package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class AuthenticationService implements IAuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;

    @Autowired
    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, IJwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public String saveUser(User userDetails){
       userDetails.setPassword(passwordEncoder.encode(new String(Base64.getDecoder().decode(userDetails.getPassword()))));
       userRepository.save(userDetails);
       return "added into db";
    }

    @Override
    public String generateToken(String username, LocalDateTime currentDateTime){
        return jwtService.generateToken(username);
    }

    @Override
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
