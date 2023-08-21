package com.CarBids.carBidsbiddingservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class CarBidsBiddingServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarBidsBiddingServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Started Car Bidding Service on port 8096"+" "+ LocalDateTime.now());
		SpringApplication.run(CarBidsBiddingServiceApplication.class, args);
	}

}
