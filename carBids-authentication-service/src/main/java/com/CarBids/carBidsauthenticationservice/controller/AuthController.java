package com.CarBids.carBidsauthenticationservice.controller;

import com.CarBids.carBidsauthenticationservice.dto.AuthRequest;
import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.service.AuthenticationService;
import com.CarBids.carBidsauthenticationservice.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final IAuthenticationService authenticationService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthenticationService authenticationService, AuthenticationManager authenticationManager){
        this.authenticationService = authenticationService;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public String addNewUser(@RequestBody User userDetails) {
        return authenticationService.saveUser(userDetails);
    }

    @GetMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return authenticationService.generateToken(authRequest.getUsername(), LocalDateTime.now());
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        authenticationService.validateToken(token);
        return "Token is valid";
    }


}
