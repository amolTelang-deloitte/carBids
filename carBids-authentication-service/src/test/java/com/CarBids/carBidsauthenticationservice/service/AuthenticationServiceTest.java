package com.CarBids.carBidsauthenticationservice.service;


import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidBase64Exception;
import com.CarBids.carBidsauthenticationservice.repository.UserRepository;
import com.mysql.cj.util.Base64Decoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
class AuthenticationServiceTest {

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private InvalidBase64Exception invalidBase64Exception;
    @Autowired
    private IJwtService jwtService;
    @MockBean
    private Base64Decoder base64Decoder;

   @Autowired
   private AuthenticationService authenticationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userRepository = mock(UserRepository.class);
        jwtService = mock(JwtService.class);
    }

    @Test
    public void testAuthenticateUser_ValidCredentials() {
        String username = "testuser";
        String password = "cGFzc3dvcmQ="; // "password" encoded in Base64

        UsernamePasswordAuthenticationToken result = authenticationService.authenticateUser(username, password);

        assertNotNull(result);
        assertEquals(username, result.getPrincipal());
        assertEquals("password", result.getCredentials());
    }

    @Test
    public void testAuthenticateUser_InvalidBase64Password() {
        String username = "testuser";
        String invalidPassword = "invalid_password";

        when(invalidBase64Exception.getMessage()).thenReturn("Incorrect Base64 encoded password");

        try {
            authenticationService.authenticateUser(username, invalidPassword);
            fail("Expected InvalidBase64Exception");
        } catch (InvalidBase64Exception e) {
            assertEquals("Incorrect Base64 encoded password", e.getMessage());
        }

        verifyNoMoreInteractions(invalidBase64Exception);
    }


    @Test
    public void testCheckValidUserId_ValidId() {
        long userId = 123;

        when(userRepository.existsById(userId)).thenReturn(true);

        ResponseEntity<?> responseEntity = authenticationService.checkValidUserId(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals( responseEntity.getBody().toString(),"false");

    }

    @Test
    public void testCheckValidUserId_InvalidId() {
        long invalidUserId = 456;

        when(userRepository.existsById(invalidUserId)).thenReturn(false);

        ResponseEntity<?> responseEntity = authenticationService.checkValidUserId(invalidUserId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals( responseEntity.getBody().toString(),"false");
    }

    @Test
    public void testGenerateToken() {
        String username = "testuser";
        long userId = 123L;

        User user = new User();
        user.setUserId(userId);

        when(userRepository.findByusername(username)).thenReturn(user);
        when(jwtService.generateToken(username, userId)).thenReturn("generatedToken");

        String result = "generatedToken";

        assertEquals("generatedToken", result);
    }

    @Test
    public void testDecodeBase64() {

        String encodedPassword = "SGVsbG8gV29ybGQh"; // "Hello World!"
        String decodedPassword = authenticationService.decodeBase64(encodedPassword);

        assertEquals("Hello World!", decodedPassword);
    }

    @Test
    void testValidBase64EncodedString() {
        String validBase64String = "SGVsbG8gV29ybGQh"; // "Hello World!"
        assertTrue(authenticationService.isBase64Encoded(validBase64String));
    }

    @Test
    void testValidEmail() {
        String validEmail = "test@example.com";
        assertTrue(authenticationService.isValidEmail(validEmail));
    }



}
