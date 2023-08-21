package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidsauthenticationservice.entity.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public interface IAuthenticationService {

    UsernamePasswordAuthenticationToken authenticateUser(String username, String password);
    ResponseEntity<?> saveUser(User userDetails);

    String generateToken(String username);

    String getUsernameFromId(long userId);
    ResponseEntity<?> checkValidUserId(long userId);

    String decodeBase64(String base64Password);


}