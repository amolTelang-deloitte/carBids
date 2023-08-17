package com.CarBids.carBidsauthenticationservice.controller;

import com.CarBids.carBidsauthenticationservice.exception.InvalidBase64Exception;
import com.CarBids.carBidscommonentites.User;
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

    @Autowired
    public AuthController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }
    

    @PostMapping("/register")
    public String addNewUser(@RequestBody User userDetails) {
        return authenticationService.saveUser(userDetails);
    }

    //use spring security default login url.

}
