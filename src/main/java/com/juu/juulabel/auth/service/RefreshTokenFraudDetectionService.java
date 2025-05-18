package com.juu.juulabel.auth.service;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.juu.juulabel.auth.domain.RefreshToken;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.AddressNotFoundException;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RefreshTokenFraudDetectionService implements FraudDetectionService<RefreshToken> {

    private static final double IP_CHANGE_WEIGHT = 0.4;
    private static final double UA_CHANGE_WEIGHT = 0.3;
    private static final double DEVICE_ID_MISMATCH_WEIGHT = 0.7;
    private static final double UNUSUAL_ACCESS_TIME_WEIGHT = 0.2;
    private static final double VELOCITY_CHANGE_WEIGHT = 0.35;
    private static final double SUSPICIOUS_UA_WEIGHT = 0.45;
    private static final double TOR_EXIT_NODE_WEIGHT = 0.6;

    private static final int SUSPICIOUS_LOGIN_DISTANCE_KM = 500;
    private static final int IMPOSSIBLE_TRAVEL_DISTANCE_KM = 1000;
    private static final Duration IMPOSSIBLE_TRAVEL_TIME = Duration.ofHours(3);

    private static final Set<String> KNOWN_TOR_EXIT_NODES = new HashSet<>();
    private static final Set<String> KNOWN_MALICIOUS_IPS = new HashSet<>();
    private static final Set<String> SUSPICIOUS_USER_AGENT_PATTERNS = new HashSet<>();

    @Value("${geoip.database.path:classpath:GeoLite2-City.mmdb}")
    private String geoipDatabasePath;

    private DatabaseReader databaseReader;

    @PostConstruct
    public void initialize() {
        try {
            // Initialize the GeoIP database reader
            File database = new File(geoipDatabasePath.replace("classpath:", ""));
            databaseReader = new DatabaseReader.Builder(database).build();

            // Initialize known Tor exit nodes (would be updated regularly in production)
            KNOWN_TOR_EXIT_NODES.add("176.10.99.200");
            KNOWN_TOR_EXIT_NODES.add("185.220.101.21");
            // More Tor exit nodes would be added here or fetched from an API

            // Initialize known malicious IPs (would be updated regularly in production)
            KNOWN_MALICIOUS_IPS.add("103.91.181.5");
            KNOWN_MALICIOUS_IPS.add("45.95.168.112");
            // More malicious IPs would be added here or fetched from a threat intelligence
            // service

            // Initialize suspicious user agent patterns
            SUSPICIOUS_USER_AGENT_PATTERNS.add("nikto");
            SUSPICIOUS_USER_AGENT_PATTERNS.add("sqlmap");
            SUSPICIOUS_USER_AGENT_PATTERNS.add("vulnerability");
            SUSPICIOUS_USER_AGENT_PATTERNS.add("masscan");
            SUSPICIOUS_USER_AGENT_PATTERNS.add("nmap");
            // More patterns would be added here

        } catch (IOException e) {
            log.error("Failed to initialize GeoIP database: {}", e.getMessage());
        }
    }

    @Override
    public RiskAssessment assessRisk(RefreshToken token, String currentIpAddress, String currentUserAgent,
            String currentDeviceId) {

        double currentScore = 0.0;
        StringBuilder reasons = new StringBuilder();
        boolean immediateFamilyCompromise = false;

        // Rule 1: IP Address Change
        if (token.getIpAddress() != null &&
                !token.getIpAddress().equals(currentIpAddress)) {
            // More sophisticated: check IP geolocation, ASN, known proxy, Tor exit node
            if (!areIpAddressesGeographicallyClose(token.getIpAddress(), currentIpAddress)) {
                currentScore += IP_CHANGE_WEIGHT;
                reasons.append("Significant IP geolocation change. ");
            }
        }

        // Rule 2: User-Agent Change
        if (token.getUserAgent() != null &&
                !token.getUserAgent().equals(currentUserAgent)) {
            currentScore += UA_CHANGE_WEIGHT;
            reasons.append("User-Agent changed. ");
        }

        // Rule 3: Device ID Mismatch
        if (token.getDeviceId() != null && currentDeviceId != null &&
                !token.getDeviceId().equals(currentDeviceId)) {
            currentScore += DEVICE_ID_MISMATCH_WEIGHT;
            reasons.append("Device ID mismatch. ");
            immediateFamilyCompromise = true; // Device ID mismatch is often a strong indicator
        } else if (token.getDeviceId() != null && currentDeviceId == null) {
            currentScore += DEVICE_ID_MISMATCH_WEIGHT * 0.5; // Device ID disappeared
            reasons.append("Device ID removed. ");
        } else if (token.getDeviceId() == null && currentDeviceId != null) {
            // New device ID added, could be a new legitimate device, lower weight or needs
            // context
            reasons.append("New Device ID added. ");
        }

        // Rule 4: Check for Tor Exit Nodes
        if (KNOWN_TOR_EXIT_NODES.contains(currentIpAddress)) {
            currentScore += TOR_EXIT_NODE_WEIGHT;
            reasons.append("Connection from known Tor exit node. ");
            immediateFamilyCompromise = true;
        }

        // Rule 5: Check for Known Malicious IPs
        if (KNOWN_MALICIOUS_IPS.contains(currentIpAddress)) {
            currentScore += 0.8; // Very high risk
            reasons.append("Connection from known malicious IP. ");
            immediateFamilyCompromise = true;
        }

        // Rule 6: Check for Suspicious User Agents
        if (currentUserAgent != null) {
            for (String pattern : SUSPICIOUS_USER_AGENT_PATTERNS) {
                if (currentUserAgent.toLowerCase().contains(pattern)) {
                    currentScore += SUSPICIOUS_UA_WEIGHT;
                    reasons.append("Suspicious User-Agent pattern detected. ");
                    break;
                }
            }
        }

        // Rule 7: Velocity Check (impossible travel)
        if (token.getIssuedAt() != null && token.getIpAddress() != null &&
                !token.getIpAddress().equals(currentIpAddress)) {

            LocalDateTime issuedAt = token.getIssuedAt();
            LocalDateTime now = LocalDateTime.now();
            Duration timeBetweenLogins = Duration.between(issuedAt, now);

            double distance = calculateDistanceBetweenIps(data.getIpAddress(), currentIpAddress);

            // If distance is very large and time between logins is short, flag as
            // impossible travel
            if (distance > IMPOSSIBLE_TRAVEL_DISTANCE_KM && timeBetweenLogins.compareTo(IMPOSSIBLE_TRAVEL_TIME) < 0) {
                currentScore += VELOCITY_CHANGE_WEIGHT;
                reasons.append("Impossible travel detected. ");
                immediateFamilyCompromise = true;
            }
        }

        return new RiskAssessment(Math.min(currentScore, 1.0), reasons.toString(), immediateFamilyCompromise);
    }

    private boolean areIpAddressesGeographicallyClose(String ip1, String ip2) {
        if (ip1.equals(ip2)) {
            return true;
        }

        // Check for internal/private IP addresses
        if (isPrivateIpAddress(ip1) || isPrivateIpAddress(ip2)) {
            return true;
        }

        double distance = calculateDistanceBetweenIps(ip1, ip2);

        // Consider IPs close if they are within a reasonable distance (e.g., 500km)
        return distance < SUSPICIOUS_LOGIN_DISTANCE_KM;
    }

    private boolean isPrivateIpAddress(String ip) {
        return ip.startsWith("192.168.") || ip.startsWith("10.") ||
                ip.startsWith("172.16.") || ip.startsWith("172.17.") ||
                ip.startsWith("172.18.") || ip.startsWith("172.19.") ||
                ip.startsWith("172.20.") || ip.startsWith("172.21.") ||
                ip.startsWith("172.22.") || ip.startsWith("172.23.") ||
                ip.startsWith("172.24.") || ip.startsWith("172.25.") ||
                ip.startsWith("172.26.") || ip.startsWith("172.27.") ||
                ip.startsWith("172.28.") || ip.startsWith("172.29.") ||
                ip.startsWith("172.30.") || ip.startsWith("172.31.") ||
                ip.equals("127.0.0.1") || ip.equals("::1") ||
                ip.equals("localhost");
    }

    private double calculateDistanceBetweenIps(String ip1, String ip2) {
        try {
            // Get locations from MaxMind GeoIP database
            CityResponse location1 = databaseReader.city(InetAddress.getByName(ip1));
            CityResponse location2 = databaseReader.city(InetAddress.getByName(ip2));

            // Get latitude and longitude from responses
            double lat1 = location1.getLocation().getLatitude();
            double lon1 = location1.getLocation().getLongitude();
            double lat2 = location2.getLocation().getLatitude();
            double lon2 = location2.getLocation().getLongitude();

            // Calculate distance using Haversine formula
            return calculateHaversineDistance(lat1, lon1, lat2, lon2);

        } catch (IOException | GeoIp2Exception e) {
            log.warn("Error calculating distance between IPs: {}", e.getMessage());
            // If we can't determine distance, assume they're not close for safety
            return Double.MAX_VALUE;
        }
    }

    // Haversine formula to calculate distance between two points on Earth
    private double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
        // Radius of Earth in kilometers
        final double R = 6371.0;

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; // Distance in kilometers
    }
}