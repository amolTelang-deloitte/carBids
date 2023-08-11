package com.CarBids.carBidsauthenticationservice.service;

import com.sun.org.apache.xpath.internal.operations.Bool;

public interface IJwtService {
    Boolean validateToken(final String token);
    String generateToken(String userName);
    String getUsernameFromToken(String token);
}
