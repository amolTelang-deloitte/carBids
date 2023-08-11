package com.CarBids.carBidsgateway.util;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Component;


@Component
public class JwtUtil {
    private  final String  SECRET ;

    public JwtUtil() {
        SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";
    }

    public Boolean validateToken(final String token){
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(SECRET)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}