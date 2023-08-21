package com.CarBids.carBidsauthenticationservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
public class CarBidsAuthenticationServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarBidsAuthenticationServiceApplication.class);
	public static void main(String[] args) {
		logger.info("Started Car Bids Authentication Service "+ " "+ LocalDateTime.now());
		SpringApplication.run(CarBidsAuthenticationServiceApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

}
