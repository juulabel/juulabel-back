package com.juu.juulabel.auth.service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.juu.juulabel.common.exception.CustomJwtException;
import com.juu.juulabel.common.exception.code.ErrorCode;
import com.juu.juulabel.member.request.ApplePublicKey;
import com.juu.juulabel.member.request.AppleUser;
import com.juu.juulabel.member.token.OAuthToken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

/**
 * Service for handling Apple JWT token operations with improved architecture.
 * Separates token parsing from business logic and provides better error handling.
 */
@Service
public class AppleTokenService {

    // Constants for better maintainability
    private static final String RSA_ALGORITHM = "RSA";
    private static final String KID_HEADER_FIELD = "kid";
    private static final String SUB_CLAIM = "sub";
    private static final String EMAIL_CLAIM = "email";
    private static final String JWT_SEPARATOR = "\\.";
    private static final int HEADER_INDEX = 0;
    private static final int EXPECTED_JWT_PARTS = 3;

    // Reuse ObjectMapper instance for better performance
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * Extracts Apple user information from JWT token using the provided public keys.
     * 
     * @param publicKeys List of Apple's public keys
     * @param oauthToken OAuth token containing the ID token
     * @return AppleUser with extracted user information
     * @throws CustomJwtException if token processing fails
     */
    public AppleUser extractAppleUser(List<ApplePublicKey> publicKeys, OAuthToken oauthToken) {
        ApplePublicKey applePublicKey = findMatchingPublicKey(publicKeys, oauthToken);
        PublicKey publicKey = buildRSAPublicKey(applePublicKey);
        JwtParser jwtParser = createJwtParser(publicKey);

        return extractFromClaims(oauthToken.idToken(), jwtParser, this::mapToAppleUser);
    }

    /**
     * Validates an Apple JWT token structure and signature.
     * 
     * @param publicKeys List of Apple's public keys
     * @param token JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean isValidAppleToken(List<ApplePublicKey> publicKeys, String token) {
        try {
            String kid = extractKidFromToken(token);
            ApplePublicKey applePublicKey = findPublicKeyByKid(publicKeys, kid);
            PublicKey publicKey = buildRSAPublicKey(applePublicKey);
            JwtParser jwtParser = createJwtParser(publicKey);
            
            parseClaims(token, jwtParser);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Extracts claims from Apple JWT token.
     * 
     * @param publicKeys List of Apple's public keys
     * @param token JWT token string
     * @return parsed Claims
     */
    public Claims extractClaims(List<ApplePublicKey> publicKeys, String token) {
        String kid = extractKidFromToken(token);
        ApplePublicKey applePublicKey = findPublicKeyByKid(publicKeys, kid);
        PublicKey publicKey = buildRSAPublicKey(applePublicKey);
        JwtParser jwtParser = createJwtParser(publicKey);
        
        return parseClaims(token, jwtParser);
    }

    // Private helper methods

    private AppleUser mapToAppleUser(Claims claims) {
        return new AppleUser(
            claims.get(SUB_CLAIM, String.class),
            claims.get(EMAIL_CLAIM, String.class)
        );
    }

    private JwtParser createJwtParser(PublicKey publicKey) {
        return Jwts.parser().verifyWith(publicKey).build();
    }

    private <T> T extractFromClaims(String token, JwtParser jwtParser, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(parseClaims(token, jwtParser));
    }

    private Claims parseClaims(String token, JwtParser jwtParser) {
        try {
            return jwtParser.parseSignedClaims(token).getPayload();
        } catch (SignatureException | MalformedJwtException ex) {
            throw new CustomJwtException(ErrorCode.JWT_MALFORMED_EXCEPTION);
        } catch (ExpiredJwtException ex) {
            throw new CustomJwtException(ErrorCode.JWT_EXPIRED_EXCEPTION);
        } catch (UnsupportedJwtException ex) {
            throw new CustomJwtException(ErrorCode.JWT_UNSUPPORTED_EXCEPTION);
        } catch (IllegalArgumentException ex) {
            throw new CustomJwtException(ErrorCode.JWT_ILLEGAL_ARGUMENT_EXCEPTION);
        }
    }

    private ApplePublicKey findMatchingPublicKey(List<ApplePublicKey> publicKeys, OAuthToken oauthToken) {
        String kid = extractKidFromToken(oauthToken.idToken());
        return findPublicKeyByKid(publicKeys, kid);
    }

    private ApplePublicKey findPublicKeyByKid(List<ApplePublicKey> publicKeys, String kid) {
        return publicKeys.stream()
                .filter(key -> kid.equals(key.kid()))
                .findFirst()
                .orElseThrow(() -> new CustomJwtException(
                        String.format("No matching Apple public key found for kid: %s", kid),
                        ErrorCode.JWT_UNSUPPORTED_EXCEPTION));
    }

    private String extractKidFromToken(String idToken) {
        try {
            String[] chunks = idToken.split(JWT_SEPARATOR);
            if (chunks.length != EXPECTED_JWT_PARTS) {
                throw new CustomJwtException("Invalid JWT format: expected 3 parts separated by dots",
                        ErrorCode.JWT_MALFORMED_EXCEPTION);
            }

            byte[] headerBytes = Base64.getUrlDecoder().decode(chunks[HEADER_INDEX]);
            String header = new String(headerBytes, StandardCharsets.UTF_8);
            JsonNode headerNode = OBJECT_MAPPER.readTree(header);

            JsonNode kidNode = headerNode.get(KID_HEADER_FIELD);
            if (kidNode == null || kidNode.isNull()) {
                throw new CustomJwtException("Missing 'kid' field in JWT header",
                        ErrorCode.JWT_MALFORMED_EXCEPTION);
            }

            return kidNode.asText();
        } catch (IllegalArgumentException e) {
            throw new CustomJwtException("Failed to decode JWT header: invalid Base64 encoding - " + e.getMessage(),
                    ErrorCode.JWT_MALFORMED_EXCEPTION);
        } catch (JsonProcessingException e) {
            throw new CustomJwtException("Failed to parse JWT header as JSON - " + e.getMessage(),
                    ErrorCode.JWT_MALFORMED_EXCEPTION);
        }
    }

    private PublicKey buildRSAPublicKey(ApplePublicKey applePublicKey) {
        try {
            byte[] nBytes = Base64.getUrlDecoder().decode(applePublicKey.n());
            byte[] eBytes = Base64.getUrlDecoder().decode(applePublicKey.e());

            BigInteger modulus = new BigInteger(1, nBytes);
            BigInteger exponent = new BigInteger(1, eBytes);

            RSAPublicKeySpec publicKeySpec = new RSAPublicKeySpec(modulus, exponent);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            return keyFactory.generatePublic(publicKeySpec);

        } catch (IllegalArgumentException e) {
            throw new CustomJwtException("Invalid Base64 encoding in Apple public key - " + e.getMessage(),
                    ErrorCode.JWT_UNSUPPORTED_EXCEPTION);
        } catch (NoSuchAlgorithmException e) {
            throw new CustomJwtException("RSA algorithm not available - " + e.getMessage(),
                    ErrorCode.JWT_UNSUPPORTED_EXCEPTION);
        } catch (InvalidKeySpecException e) {
            throw new CustomJwtException("Invalid RSA key specification - " + e.getMessage(),
                    ErrorCode.JWT_UNSUPPORTED_EXCEPTION);
        }
    }
} 