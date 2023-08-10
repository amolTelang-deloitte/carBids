package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidsauthenticationservice.entity.User;
import java.time.LocalDateTime;

public interface IAuthenticationService {
    String saveUser(User userDetails);

    String generateToken(String username, LocalDateTime currentDateTime);

    void validateToken(String token);
}