package com.CarBids.carBidsauthenticationservice.controller;

import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.repository.UserRepository;
import com.CarBids.carBidsauthenticationservice.service.AuthenticationService;
import com.CarBids.carBidsauthenticationservice.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest
class AuthControllerTest {

    private AuthenticationService authenticationService = mock(AuthenticationService.class);
    private AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    private AuthController authenticationController = new AuthController(authenticationService, authenticationManager);

    @Test
    void testAuthenticateUser_Success() {
        String username = "test2";
        String password = "cGFzc3dvcmQ=";
        String generatedToken = "generatedToken";

        UserDetails userDetails = mock(UserDetails.class);
        Authentication authentication = mock(Authentication.class);
        when(authenticationService.authenticateUser(username, password)).thenReturn(new UsernamePasswordAuthenticationToken(username, password));
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(username);
        when(authenticationService.generateToken(username)).thenReturn(generatedToken);

        ResponseEntity<String> response = authenticationController.authenticateUser(username, password);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(generatedToken, response.getBody());
        verify(authenticationService).authenticateUser(username, password);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(authenticationService).generateToken(username);
        verifyNoMoreInteractions(authenticationService, authenticationManager);
    }

    @Test
    void testAddNewUser_Success() {
        User user = new User(); // Create a user instance for testing
        when(authenticationService.saveUser(user)).thenReturn(ResponseEntity.status(HttpStatus.CREATED).build());

        ResponseEntity<?> response = authenticationController.addNewUser(user);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(authenticationService).saveUser(user);
        verifyNoMoreInteractions(authenticationService);
    }

    @Test
    void testGetUsername_Success() {
        Long userId = 123L;
        String expectedUsername = "testuser";

        when(authenticationService.getUsernameFromId(userId)).thenReturn(expectedUsername);

        String actualUsername = authenticationController.getUsername(userId);

        assertEquals(expectedUsername, actualUsername);
        verify(authenticationService).getUsernameFromId(userId);
        verifyNoMoreInteractions(authenticationService);
    }

    @Test
    void testCheckUserId_Valid() {
        Long validUserId = 123L;

        when(authenticationService.checkValidUserId(validUserId)).thenReturn(ResponseEntity.ok().build());

        ResponseEntity<?> response = authenticationController.checkUserId(validUserId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(authenticationService).checkValidUserId(validUserId);
        verifyNoMoreInteractions(authenticationService);
    }


}
