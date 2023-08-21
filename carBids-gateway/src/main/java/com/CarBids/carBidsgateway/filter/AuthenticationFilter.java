package com.CarBids.carBidsgateway.filter;

import com.CarBids.carBidsgateway.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private final RouteValidator validator;

    private final JwtUtil jwtUtil;

    private Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);


    public AuthenticationFilter(JwtUtil jwtUtil, RouteValidator validator) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
        this.validator = validator;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            if (validator.isSecured.test(exchange.getRequest())) {
                //check if token present on header or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    logger.error("Required 'Authorization' header is missing. Access to certain resources may be denied.");
                    throw new RuntimeException("missing authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                }
                if(!jwtUtil.validateToken(authHeader)){
                    logger.warn("Invalid JWT token received.Access to certain resources may be denied.");
                    throw new RuntimeException("Un Authorized access to application.");
                }
            }
            logger.info("JWT token verified successfully.");
            return chain.filter(exchange);
        });
    }

    public static class Config {

    }
}
