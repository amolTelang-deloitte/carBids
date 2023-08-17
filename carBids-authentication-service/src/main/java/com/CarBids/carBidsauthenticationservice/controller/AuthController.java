package com.CarBids.carBidsauthenticationservice.controller;

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
        UsernamePasswordAuthenticationToken authenticationToken = authenticationService.authenticateUser(username,password);
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String access_token = authenticationService.generateToken(userDetails.getUsername());
            return ResponseEntity.ok().body(access_token);
        } catch (AuthenticationException e) {
            // Handle authentication failure
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed");
        }
    }
    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@RequestBody User userDetails) {
        return authenticationService.saveUser(userDetails);
    }

    //use spring security default login url.

}
