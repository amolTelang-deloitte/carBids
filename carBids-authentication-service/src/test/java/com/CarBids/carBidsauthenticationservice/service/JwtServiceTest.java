package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    private JwtService jwtServiceUnderTest;
    @Mock
    private Keys keys;
    @Mock
    private UserRepository userRepository;
    @BeforeEach
    void setUp() {
        jwtServiceUnderTest = new JwtService();
    }


    @Test
    void testGenerateToken() {
        // Prepare test data
        String userName = "testUser";
        Long userId = 123L;
        String token = jwtServiceUnderTest.generateToken(userName, userId);
        assertEquals(false, token.isEmpty());
    }

    @Test
    void testGetUsernameFromToken() {
        assertThat(jwtServiceUnderTest.getUsernameFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjkyNjk0Mzg3LCJleHAiOjE2OTI2OTYxODd9.Qk5R5ME2GHrPqjK4ylT8K23qgVi0U7yhA_kyY5Vbo8Y")).isEqualTo("1");
    }
    @Test
    public void testCreateToken() {
        Map<String, Object> claims = new HashMap<>();
        String userName = "testUser";
        Long userId = 123L;

        LocalDateTime now = LocalDateTime.now();
        Date nowDate = new Date();

        String token = jwtServiceUnderTest.createToken(claims, userName, userId);

        assertDoesNotThrow(() -> Jwts.parser().setSigningKey(jwtServiceUnderTest.getSignKey()).parseClaimsJws(token));
    }

    @Test
    void testGetSignKey() {
        Key key = jwtServiceUnderTest.getSignKey();
        assertNotNull(key, "Key should not be null");
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        Key expectedKey = Keys.hmacShaKeyFor(keyBytes);
        assertEquals(expectedKey, key, "Generated key should match expected key");
    }


}
