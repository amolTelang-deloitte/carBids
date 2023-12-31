package com.CarBids.carBidslotsservice.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class JwtUtil {

    private  final Logger logger = LoggerFactory.getLogger(JwtUtil.class);
    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder().setSigningKey(SECRET).build().parseClaimsJws(token).getBody();
        logger.info("UserId retrived from JWT token" + " "+ LocalDateTime.now());
        return claims.getSubject();
    }
}
