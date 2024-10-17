package com.rose.online_learning_platform.auth.services;

import com.rose.online_learning_platform.auth.dto.JwtTokenOutput;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class JwtAuthService {
    private Object getKeyFile(String path, String keyType) {
        try {
            KeyFactory kf = KeyFactory.getInstance("RSA");
            ClassPathResource classPathResource = new ClassPathResource(path);
            byte[] res = classPathResource.getInputStream().readAllBytes();
            String data = new String(res, StandardCharsets.UTF_8);
            // Remove the first and last lines and any whitespace
            String pemFile =
                    data.replace("-----BEGIN " + keyType + " KEY-----", "")
                    .replace("-----END " + keyType + " KEY-----", "")
                            .replaceAll(System.lineSeparator(), "")
                            .replaceAll("\r\n", "")
                            .replaceAll("\n", "")
                            .replaceAll("\r", "")
                            .replaceAll("\\s+", ""); // Remove all whitespace

            byte [] decodedKey = Base64.getDecoder().decode(pemFile);
            if ("PRIVATE".equals(keyType)) {
                PKCS8EncodedKeySpec keySpecPKCS8 = new PKCS8EncodedKeySpec(decodedKey);
                return kf.generatePrivate(keySpecPKCS8);
            } else {
                X509EncodedKeySpec keySpecX509 = new X509EncodedKeySpec(decodedKey);
                return kf.generatePublic(keySpecX509);
            }
        } catch (Exception ex) {
            log.error("Error reading key file", ex);
            throw new RuntimeException("Internal Server Error");
        }
    }

    public String extractUsername(String token) {
        return extractClaim(token,Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims,T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public JwtTokenOutput generateJwtResponse(UserDetails userDetails){
        String accessToken = generateAccessToken(new HashMap<>(), userDetails);
        String refreshToken = generateRefreshToken(new HashMap<>(), userDetails);
        Claims claims = extractAllClaims(accessToken);

        return new JwtTokenOutput()
                .setAccessToken(accessToken)
                .setRefreshToken(refreshToken)
                .setTokenType("Bearer")
                .setExpiresIn(claims.getExpiration())
                .setIssuedAt(claims.getIssuedAt());
    }

    public String generateToken(Map<String, Objects> extraClaims,
                         UserDetails userDetails, Date expiration) {
        return Jwts
                .builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expiration)
                .signWith(SignatureAlgorithm.RS512, getPrivateKeyFromFile())
                .compact();
    }

    public String generateAccessToken(Map<String, Objects> extraClaims,
            UserDetails userDetails){
        return generateToken(extraClaims, userDetails, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24));
    }

    public String generateRefreshToken(Map<String, Objects> extraClaims, UserDetails userDetails){
        return generateToken(extraClaims, userDetails, new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7));
    }

    private PrivateKey getPrivateKeyFromFile() {
        return (PrivateKey) getKeyFile("certs/private.pem", "PRIVATE");
    }

    private PublicKey getPublicKeyFromFile() {
        return (PublicKey) getKeyFile("certs/public.pem", "PUBLIC");
    }

    public boolean isTokenValid(String token, UserDetails userDetails){
        final String username = extractUsername(token);
        // TODO: Check the user details match with what is in the auth token repository
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parser()
                .setSigningKey(getPublicKeyFromFile())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}