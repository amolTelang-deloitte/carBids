package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidscommonentites.User;



public interface IAuthenticationService {
    String saveUser(User userDetails);

    String generateToken(String username);

    String decodeBase64(String base64Password);

}