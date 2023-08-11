package com.CarBids.carBidsauthenticationservice.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void getUserId() {
        User user = new User();
        user.setUserId(1L);
        assertEquals(1L, user.getUserId());
    }

    @Test
    void getUsername() {
        User user = new User();
        user.setUsername("username");
        assertEquals("username", user.getUsername());
    }

    @Test
    void getFirstName() {
        User user = new User();
        user.setFirstName("John");
        assertEquals("John", user.getFirstName());
    }

    @Test
    void getLastName() {
        User user = new User();
        user.setLastName("Doe");
        assertEquals("Doe", user.getLastName());
    }

    @Test
    void getEmail() {
        User user = new User();
        user.setEmail("john@example.com");
        assertEquals("john@example.com", user.getEmail());
    }

    @Test
    void getPhoneNumber() {
        User user = new User();
        user.setPhoneNumber("1234567890");
        assertEquals("1234567890", user.getPhoneNumber());
    }

    @Test
    void getPassword() {
        User user = new User();
        user.setPassword("password");
        assertEquals("password", user.getPassword());
    }

    @Test
    void testEquals() {
        User user1 = new User(1L, "username", "John", "Doe", "john@example.com", "1234567890", "password");
        User user2 = new User(1L, "username", "John", "Doe", "john@example.com", "1234567890", "password");
        assertTrue(user1.equals(user2));
    }

    @Test
    void testHashCode() {
        User user = new User(1L, "username", "John", "Doe", "john@example.com", "1234567890", "password");
        int hashCode = user.hashCode();
        assertNotNull(hashCode);
    }

    @Test
    void testToString() {
        User user = new User(1L, "username", "John", "Doe", "john@example.com", "1234567890", "password");
        assertNotNull(user.toString());
    }
}