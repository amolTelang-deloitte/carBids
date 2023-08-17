package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidscommonentites.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;


public interface IAuthenticationService {

    UsernamePasswordAuthenticationToken authenticateUser(String username, String password);
    ResponseEntity saveUser(User userDetails);

    String generateToken(String username);

    String decodeBase64(String base64Password);


}