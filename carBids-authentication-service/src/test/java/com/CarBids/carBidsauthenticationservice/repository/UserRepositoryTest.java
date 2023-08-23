package com.CarBids.carBidsauthenticationservice.repository;

import com.CarBids.carBidsauthenticationservice.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSaveUser() {
        User user = new User(null, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        User savedUser = userRepository.save(user);
        assertNotNull(savedUser.getUserId());
    }

    @Test
    public void testFindByUsername() {
        User user = new User(null, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        userRepository.save(user);

        User foundUser = userRepository.findByusername("testuser");
        assertNotNull(foundUser);
        assertEquals("testuser", foundUser.getUsername());
    }

    @Test
    public void testExistsByUsername() {
        User user = new User(null, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        userRepository.save(user);

        assertTrue(userRepository.existsByusername("testuser"));
        assertFalse(userRepository.existsByusername("nonexistentuser"));
    }

    @Test
    public void testExistsByEmailOrPhoneNumber() {
        User user = new User(null, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmailOrPhoneNumber("john@example.com", "1234567890"));
        assertFalse(userRepository.existsByEmailOrPhoneNumber("nonexistent@example.com", "9876543210"));
    }

    @Test
    public void testUpdateUser() {
        User user = new User(null, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        User savedUser = userRepository.save(user);

        savedUser.setFirstName("UpdatedFirstName");
        userRepository.save(savedUser);

        User updatedUser = userRepository.findByusername("testuser");
        assertEquals("UpdatedFirstName", updatedUser.getFirstName());
    }

    @Test
    public void testDeleteUser() {
        User user = new User(null, "testuser", "John", "Doe", "john@example.com", "1234567890", "password");
        User savedUser = userRepository.save(user);

        userRepository.delete(savedUser);
        assertFalse(userRepository.existsByusername("testuser"));
    }
}
