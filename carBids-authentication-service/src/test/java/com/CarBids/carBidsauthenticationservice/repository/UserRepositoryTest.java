package com.CarBids.carBidsauthenticationservice.repository;

import com.CarBids.carBidsauthenticationservice.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserRepositoryTest {

    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryTest(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Test
    void findByUsername() {
        // Given
        User user = new User(1L, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        userRepository.save(user);

        // When
        Optional<User> foundUser = userRepository.findByusername("testuser");

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals("testuser", foundUser.get().getUsername());
        assertEquals("John", foundUser.get().getFirstName());
        assertEquals("Doe", foundUser.get().getLastName());
        assertEquals("john@example.com", foundUser.get().getEmail());
        assertEquals("1234567890", foundUser.get().getPhoneNumber());
        assertEquals("password", foundUser.get().getPassword());
    }

    @Test
    void findByUsername_NotFound() {
        // When
        Optional<User> foundUser = userRepository.findByusername("nonexistentuser");

        // Then
        assertFalse(foundUser.isPresent());
    }
}