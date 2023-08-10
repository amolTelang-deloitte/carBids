package com.CarBids.carBidsauthenticationservice.service;

public interface IJwtService {
    void validateToken(final String token);
    String generateToken(String userName);
}
