package com.CarBids.carBidsauthenticationservice.controller;

import com.CarBids.carBidscommonentites.User;
import com.CarBids.carBidsauthenticationservice.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class AuthController {
    private final IAuthenticationService authenticationService;

    @Autowired
    public AuthController(IAuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestParam String username, @RequestParam String password) {
        return authenticationService.authenticateUser(username,password);
    }
    @PostMapping("/register")
    public String addNewUser(@RequestBody User userDetails) {
        return authenticationService.saveUser(userDetails);
    }
    

}
