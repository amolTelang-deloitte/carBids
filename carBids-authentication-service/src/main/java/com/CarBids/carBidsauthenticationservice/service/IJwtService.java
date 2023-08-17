package com.CarBids.carBidsauthenticationservice.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

public interface IJwtService {
    String generateToken(String userName,Long userId);
    String getUsernameFromToken(String token);
}
