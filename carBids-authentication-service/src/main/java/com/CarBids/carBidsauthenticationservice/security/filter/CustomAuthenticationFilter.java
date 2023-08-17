package com.CarBids.carBidsauthenticationservice.security.filter;

import com.CarBids.carBidsauthenticationservice.exception.InvalidBase64Exception;
import com.CarBids.carBidsauthenticationservice.exception.exceptions.InvalidPasswordException;
import com.CarBids.carBidsauthenticationservice.service.IAuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import java.util.regex.Pattern;


public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final IAuthenticationService authenticationService;

    @Autowired
    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, IAuthenticationService authenticationService){
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        setFilterProcessesUrl("/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
            String username = request.getParameter("username");
            String password = new String(Base64.getDecoder().decode(request.getParameter("password")));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException, InvalidPasswordException {
            User user = (User)authentication.getPrincipal();
            String access_token = authenticationService.generateToken(user.getUsername());
            response.setHeader("access_token",access_token);

    }

    public boolean isBase64Encoded(String input) {
        try {
            Base64.getDecoder().decode(input);
            // Check if the string matches the Base64 pattern (optional)
            return Pattern.matches("^[A-Za-z0-9+/]*[=]{0,2}$", input);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
