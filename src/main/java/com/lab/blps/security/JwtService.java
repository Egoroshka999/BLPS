package com.lab.blps.security;


import com.lab.blps.models.applications.User;
import com.lab.blps.utils.CustomUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    private String secret = "L4OAnNc6P1N/GGosJYxNg5jOLbFHtOUhJaAhJ47IE5V9V+kRoEGOtCiufO7ALkHWoLT4jItzgXOaNMMer9dDHPFgAbfhi1uUr17VTs6VnnAGq2Xf0h009OBC2qQiyvUO3xmDnFslIm3VAqm15bDopzq5/5UPqKJ3FX/8ZideLvA4DCr/n8luDXWkVHFYjFC4SwIHZHffagQpp07BLBjeszXBiNAhnS4Ok5O1ZpmmsM2FN7OiRKeojevUTr2MIgF9tXOIDoUaqTU/bgki2I0naWw7jPFzDoUESCpxLQey8rVuotIeQyMDbnKqKGI+Wv2E3skxUr+Xc/cM3HsS6VQ==";
    private long jwtExpirationInMs = 3600000; // 1 час

    /**
     * Извлечение имени пользователя из токена
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public Integer extractId(String token) {
        return extractClaim(token, claims -> (Integer) claims.get("id"));
    }

    public String extractRole(String token) {
        return extractClaim(token, claims -> (String) claims.get("role"));
    }

    /**
     * Генерация токена
     */
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof CustomUserDetails customUserDetails) {
            User user = customUserDetails.getUser();
            claims.put("id", user.getId());
            claims.put("role", user.getRole().name());
        }

        return generateToken(claims, userDetails);
    }

    /**
     * Проверка токена на валидность
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return !isTokenExpired(token) && (username.equals(userDetails.getUsername()));
    }

    /**
     * Извлечение данных из токена
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерация токена
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .claims(extraClaims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationInMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Проверка токена на просроченность
     *
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлечение даты истечения токена
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлечение всех данных из токена
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получение ключа для подписи токена
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
