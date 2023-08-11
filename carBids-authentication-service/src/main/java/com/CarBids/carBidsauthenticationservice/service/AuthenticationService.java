package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidsauthenticationservice.entity.User;
import com.CarBids.carBidsauthenticationservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.Optional;


@Service
public class AuthenticationService implements IAuthenticationService, UserDetailsService {
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
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByusername(username);
        User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        //returning User from spring security userDetails package
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

    @Override
    public String generateToken(String username){
        return jwtService.generateToken(username);
    }

    @Override
    public void validateToken(String token){
        jwtService.validateToken(token);
    }
}
