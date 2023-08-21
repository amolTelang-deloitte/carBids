package com.CarBids.carBidsauthenticationservice.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtServiceUnderTest;

    @BeforeEach
    void setUp() {
        jwtServiceUnderTest = new JwtService();
    }

    @Test
    void testGenerateToken() {
        assertThat(jwtServiceUnderTest.generateToken("userName", 0L)).startsWith("e");;
    }

    @Test
    void testGetUsernameFromToken() {
        assertThat(jwtServiceUnderTest.getUsernameFromToken("eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiaWF0IjoxNjkyNTYzMzg1LCJleHAiOjE2OTI1NjUxODV9.CE3gn0CtNusgcbBfgSj-JkKGVOcYYxrE_fL5gJ1pmkk")).isEqualTo("2");
    }
}
