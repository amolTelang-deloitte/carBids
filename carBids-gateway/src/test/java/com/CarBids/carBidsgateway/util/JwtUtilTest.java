package com.CarBids.carBidsgateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class JwtUtilTest {
    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private static final Key SECRET_KEY = Keys.hmacShaKeyFor(SECRET.getBytes());

    @Mock
    private JwtUtil jwtUtil;

    @Test
    public void testValidToken() {
        String validToken = generateValidToken();
        TokenValidator validator = new TokenValidator();

        assertFalse(jwtUtil.validateToken(validToken));
    }

    @Test
    public void testInvalidToken() {
        String invalidToken = generateInvalidToken();
        TokenValidator validator = new TokenValidator();

        assertFalse(jwtUtil.validateToken(invalidToken));
    }


    private String generateValidToken() {
        return Jwts.builder()
                .setSubject("user123")
                .signWith(SECRET_KEY)
                .compact();
    }

    private String generateInvalidToken() {
        return "invalid_token_format";
    }

    private static class TokenValidator {
        public Boolean validateToken(final String token) {
            try {
                Jws<Claims> claimsJws = Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (JwtException e) {
                return false;
            }
        }
    }

}