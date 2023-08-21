package com.CarBids.carBidsauthenticationservice.controller;

import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.service.AuthenticationService;
import com.CarBids.carBidsauthenticationservice.service.IAuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);
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
                    logger.info("Username"+" "+ username +" and password verified successfully" + LocalDateTime.now());
                    return ResponseEntity.ok().body(access_token);
                } catch (AuthenticationException e) {
                    logger.error("Invalid Credentials Entered"+" "+LocalDateTime.now());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Credentials ! please");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> addNewUser(@RequestBody User userDetails) {
        logger.info("Attempting to add User details into the database"+" "+LocalDateTime.now());
        return authenticationService.saveUser(userDetails);
    }

    @GetMapping("/get/username")
    public String getUsername(@RequestParam(required = true)Long userId){
        logger.info("Attempting to get username using userId from the database"+" "+LocalDateTime.now());
        return authenticationService.getUsernameFromId(userId);
    }

    @GetMapping("/check/userId")
    public ResponseEntity<?> checkUserId(@RequestParam(required = true)Long userId){
        logger.info("Attempting to validate userId "+" "+LocalDateTime.now());
        return authenticationService.checkValidUserId(userId);
    }

}
