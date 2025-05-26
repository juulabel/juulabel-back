package com.juu.juulabel.common.util;

import jakarta.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for IP address extraction and validation
 */
public final class IpAddressExtractor extends AbstractHttpUtil {

    private static final String UNKNOWN = "unknown";

    // Ordered by reliability - most trusted first
    private static final List<String> IP_HEADER_CANDIDATES = List.of(
            "CF-Connecting-IP", // Cloudflare (most reliable if using CF)
            "True-Client-IP", // Akamai
            "X-Real-IP", // Nginx proxy
            "X-Forwarded-For", // Standard but easily spoofed
            "X-Cluster-Client-IP", // Google Cloud
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR");

    // IPv4 pattern
    private static final Pattern IPV4_PATTERN = Pattern.compile(
            "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");

    // IPv6 pattern (simplified)
    private static final Pattern IPV6_PATTERN = Pattern.compile(
            "^(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|:((:[0-9a-fA-F]{1,4}){1,7}|:)|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4})$");

    /**
     * Private constructor to prevent instantiation
     */
    private IpAddressExtractor() {
        super();
    }

    /**
     * Extract client IP address with validation and reliability checks
     * 
     * @return most reliable client IP address found
     */
    public static String getClientIpAddress() {
        HttpServletRequest request = getCurrentRequest();

        return IP_HEADER_CANDIDATES.stream()
                .map(request::getHeader)
                .filter(ip -> ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip))
                .map(ip -> ip.split(",")[0].trim()) // Take first IP from comma-separated list
                .filter(IpAddressExtractor::isValidIpAddress)
                .filter(IpAddressExtractor::isPublicIpAddress) // Prefer public IPs
                .findFirst()
                .orElseGet(() -> {
                    // Fallback: try to get any valid IP (including private)
                    String fallbackIp = IP_HEADER_CANDIDATES.stream()
                            .map(request::getHeader)
                            .filter(ip -> ip != null && !ip.isEmpty() && !UNKNOWN.equalsIgnoreCase(ip))
                            .map(ip -> ip.split(",")[0].trim())
                            .filter(IpAddressExtractor::isValidIpAddress)
                            .findFirst()
                            .orElse(request.getRemoteAddr());

                    return fallbackIp != null ? fallbackIp : "unknown";
                });
    }

    /**
     * Get client IP with reliability score for monitoring/logging
     */
    public static IpAddressInfo getClientIpAddressWithInfo() {
        HttpServletRequest request = getCurrentRequest();

        for (int i = 0; i < IP_HEADER_CANDIDATES.size(); i++) {
            String headerName = IP_HEADER_CANDIDATES.get(i);
            String headerValue = request.getHeader(headerName);

            if (headerValue != null && !headerValue.isEmpty() && !UNKNOWN.equalsIgnoreCase(headerValue)) {
                String ip = headerValue.split(",")[0].trim();
                if (isValidIpAddress(ip)) {
                    ReliabilityLevel reliability = getReliabilityLevel(headerName, ip);
                    return new IpAddressInfo(ip, headerName, reliability);
                }
            }
        }

        String remoteAddr = request.getRemoteAddr();
        return new IpAddressInfo(
                remoteAddr != null ? remoteAddr : "unknown",
                "REMOTE_ADDR",
                ReliabilityLevel.LOW);
    }

    /**
     * Validate if string is a valid IP address (IPv4 or IPv6)
     */
    private static boolean isValidIpAddress(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            return false;
        }

        try {
            InetAddress.getByName(ip);
            return IPV4_PATTERN.matcher(ip).matches() || IPV6_PATTERN.matcher(ip).matches();
        } catch (UnknownHostException e) {
            return false;
        }
    }

    /**
     * Check if IP address is public (not private/local)
     */
    private static boolean isPublicIpAddress(String ip) {
        if (!isValidIpAddress(ip)) {
            return false;
        }

        return !isPrivateIpAddress(ip) && !isSpecialAddress(ip);
    }

    /**
     * Check if IP is in private ranges
     */
    private static boolean isPrivateIpAddress(String ip) {
        if (ip.startsWith("10.") || ip.startsWith("192.168.")) {
            return true;
        }

        if (ip.startsWith("172.")) {
            return isPrivate172Range(ip);
        }

        return false;
    }

    /**
     * Check if 172.x.x.x IP is in private range (172.16.0.0 to 172.31.255.255)
     */
    private static boolean isPrivate172Range(String ip) {
        String[] octets = ip.split("\\.");
        if (octets.length < 2) {
            return false;
        }

        try {
            int secondOctet = Integer.parseInt(octets[1]);
            return secondOctet >= 16 && secondOctet <= 31;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Check if IP is localhost or other special addresses
     */
    private static boolean isSpecialAddress(String ip) {
        return ip.equals("127.0.0.1") || ip.equals("::1") || ip.equals("0.0.0.0");
    }

    private static ReliabilityLevel getReliabilityLevel(String headerName, String ip) {
        // Rate headers by trustworthiness
        return switch (headerName) {
            case "CF-Connecting-IP", "True-Client-IP" -> ReliabilityLevel.HIGH;
            case "X-Real-IP", "X-Cluster-Client-IP" -> ReliabilityLevel.MEDIUM;
            case "X-Forwarded-For" -> isPublicIpAddress(ip) ? ReliabilityLevel.MEDIUM : ReliabilityLevel.LOW;
            default -> ReliabilityLevel.LOW;
        };
    }

    /**
     * Data class for IP address information
     */
    public static class IpAddressInfo {
        private final String ipAddress;
        private final String sourceHeader;
        private final ReliabilityLevel reliability;

        public IpAddressInfo(String ipAddress, String sourceHeader, ReliabilityLevel reliability) {
            this.ipAddress = ipAddress;
            this.sourceHeader = sourceHeader;
            this.reliability = reliability;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public String getSourceHeader() {
            return sourceHeader;
        }

        public ReliabilityLevel getReliability() {
            return reliability;
        }
    }

    public enum ReliabilityLevel {
        HIGH, // Cloudflare, Akamai - very reliable
        MEDIUM, // Nginx, proper proxies - generally reliable
        LOW // Easy to spoof headers - use with caution
    }
}