package com.CarBids.carBidsauthenticationservice.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class UserTest {

    @Test
    void testUserIdGetterAndSetter() {
        User user = new User();
        user.setUserId(1L);
        assertEquals(1L, user.getUserId());
    }

    @Test
    void testUsernameGetterAndSetter() {
        User user = new User();
        user.setUsername("testuser");
        assertEquals("testuser", user.getUsername());
    }

    // Repeat similar tests for other properties like firstName, lastName, email, phoneNumber, password

    @Test
    void testNoArgsConstructor() {
        User user = new User();
        assertNotNull(user);
    }

    @Test
    void testAllArgsConstructor() {
        User user = new User(1L, "testuser", "John", "Doe", "johndoe@example.com", "123456789", "password");
        assertNotNull(user);
        assertEquals(1L, user.getUserId());
        assertEquals("testuser", user.getUsername());
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("johndoe@example.com", user.getEmail());
        assertEquals("123456789", user.getPhoneNumber());
        assertEquals("password", user.getPassword());
    }

    @Test
    void testToString() {
        User user = new User(1L, "testuser", "John", "Doe", "johndoe@example.com", "123456789", "password");
        String expectedToString = "User(userId=1, username=testuser, firstName=John, lastName=Doe, " +
                "email=johndoe@example.com, phoneNumber=123456789, password=password)";
        assertEquals(expectedToString, user.toString());
    }

    // You can also write tests for custom methods or business logic related to the User entity

}
