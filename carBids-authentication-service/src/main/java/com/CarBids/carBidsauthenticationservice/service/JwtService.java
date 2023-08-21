package com.CarBids.carBidsauthenticationservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService implements IJwtService {
    private final Logger logger = LoggerFactory.getLogger(JwtService.class);
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    @Override
    public String generateToken(String userName,Long userId) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userName,userId);
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token).getBody();
        logger.info("Getting username from JWT Token"+" "+ LocalDateTime.now());
        return claims.getSubject();
    }

    private String createToken(Map<String, Object> claims, String userName,Long userId) {
        logger.info("Generating JWT Token"+" "+ LocalDateTime.now());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setSubject(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
