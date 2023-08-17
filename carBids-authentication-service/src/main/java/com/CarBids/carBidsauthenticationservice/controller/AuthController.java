package com.CarBids.carBidsauthenticationservice.controller;

import com.CarBids.carBidsauthenticationservice.exception.InvalidBase64Exception;
import com.CarBids.carBidscommonentites.User;
import com.CarBids.carBidsauthenticationservice.service.AuthenticationService;
import com.CarBids.carBidsauthenticationservice.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
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

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestParam String username, @RequestParam String password) {
        String decodedPassword = authenticationService.decodeBase64(password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, decodedPassword);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String access_token = authenticationService.generateToken(userDetails.getUsername());
            String modifiedResponse = "Custom message or JSON structure with the access token";
            return ResponseEntity.ok().body(access_token);
        } catch (AuthenticationException e) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
    @PostMapping("/register")
    public String addNewUser(@RequestBody User userDetails) {
        return authenticationService.saveUser(userDetails);
    }

    //use spring security default login url.

}
