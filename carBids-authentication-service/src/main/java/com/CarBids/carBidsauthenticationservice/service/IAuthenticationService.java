package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidscommonentites.User;
import org.springframework.http.ResponseEntity;


public interface IAuthenticationService {

    ResponseEntity<?> authenticateUser(String username, String password);
    String saveUser(User userDetails);

    String generateToken(String username);

    String decodeBase64(String base64Password);


}