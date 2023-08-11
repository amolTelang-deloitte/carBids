package com.CarBids.carBidsgateway.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {
    public static final Key SECRET = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS256);

    public void validateToken(final String token){
             Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token);
    }

}