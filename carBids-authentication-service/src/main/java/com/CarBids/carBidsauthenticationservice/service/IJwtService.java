package com.CarBids.carBidsauthenticationservice.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

public interface IJwtService {
    String generateToken(String userName);
    String getUsernameFromToken(String token);
}
