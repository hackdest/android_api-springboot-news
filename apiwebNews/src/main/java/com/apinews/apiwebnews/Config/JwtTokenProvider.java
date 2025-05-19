package com.apinews.apiwebnews.Config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long EXPIRATION_TIME;

    private Key signingKey;

    @PostConstruct
    public void init() {
        if (!isBase64(secretKey)) {
            System.out.println("⚠️ SECRET_KEY không hợp lệ! Mã hóa lại...");
            secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        }
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)  // ✅ Lưu role vào token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    public String getRoleFromToken(String token) {
        return parseClaims(token).get("role", String.class);
    }

    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("❌ Token đã hết hạn!");
        } catch (MalformedJwtException e) {
            System.out.println("❌ Token không hợp lệ!");
        } catch (UnsupportedJwtException e) {
            System.out.println("❌ Token không được hỗ trợ!");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ Token trống hoặc sai định dạng!");
        }
        return false;
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public Key getSigningKey() {
        return signingKey;
    }

    private boolean isBase64(String key) {
        try {
            Base64.getDecoder().decode(key);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
