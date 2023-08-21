package com.CarBids.carBidsauthenticationservice.service;

import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidBase64Exception;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidCredentialsException;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidIdException;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.UserAlreadyExistsException;
import com.CarBids.carBidsauthenticationservice.repository.UserRepository;
import com.CarBids.carBidsauthenticationservice.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Base64;
import java.util.regex.Pattern;


@Service
public class AuthenticationService implements IAuthenticationService, UserDetailsService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final IJwtService jwtService;

    @Autowired
    public AuthenticationService(UserRepository userRepository,PasswordEncoder passwordEncoder, IJwtService jwtService){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public UsernamePasswordAuthenticationToken authenticateUser(String username, String password){

        if(!isBase64Encoded(password)){
            throw new InvalidBase64Exception("Incorrect Base64 encoded password");
        }
        String decodedPassword = new String(Base64.getDecoder().decode(password));
        return  new UsernamePasswordAuthenticationToken(username, decodedPassword);
    }

    @Override
    public ResponseEntity<?> saveUser(User userDetails){
        if(userRepository.existsByEmailOrPhoneNumber(userDetails.getEmail(),userDetails.getPhoneNumber())){
            throw new UserAlreadyExistsException("User with same credentials already exists");
        }
        if(!isBase64Encoded(userDetails.getPassword())){
            throw new InvalidBase64Exception("Incorrect Base64 encoded password");
        }
        if(userRepository.existsByusername(userDetails.getUsername())){
            throw new UserAlreadyExistsException("Username already exists choose a differnt one");
        }
        if(!isValidEmail(userDetails.getEmail()))
        {
            throw new InvalidCredentialsException("Invalid Email has been entered");
        }
        if(isValidPhoneNo(userDetails.getPhoneNumber())){
            throw new InvalidCredentialsException("Invalid Phone Number has been entered");
        }
       userDetails.setPassword(passwordEncoder.encode(new String(Base64.getDecoder().decode(userDetails.getPassword()))));
       userRepository.save(userDetails);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User added successfully");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByusername(username);
        //User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities("ROLE_USER")
                .build();
    }

    @Override
    public String getUsernameFromId(long userId) {
        User user = userRepository.findById(userId).orElseThrow(()->new InvalidIdException("Invalid User Exception"));
        String username= user.getUsername();
        return username;
    }

    @Override
    public ResponseEntity<?> checkValidUserId(long userId) {
        return new ResponseEntity<>(userRepository.existsById(userId), HttpStatus.OK) ;
    }

    @Override
    public String generateToken(String username){
        User userOptional = userRepository.findByusername(username);
        return jwtService.generateToken(username,userOptional.getUserId());
    }

    @Override
    public String decodeBase64(String password){
       return new String(Base64.getDecoder().decode(password));
    }

    public static boolean isBase64Encoded(String input) {
        try {
            Base64.getDecoder().decode(input);
            return Pattern.matches("^[A-Za-z0-9+/]*[=]{0,2}$", input);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }

    public static boolean isValidPhoneNo(String phno){
        return Pattern.matches("[0-9]{10}", phno);
    }

    public static boolean isValidEmail(String email){
        return Pattern.matches("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
    }

}
