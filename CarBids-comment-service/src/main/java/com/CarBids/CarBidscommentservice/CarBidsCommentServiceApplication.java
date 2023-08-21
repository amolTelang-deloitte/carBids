package com.CarBids.CarBidscommentservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class CarBidsCommentServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarBidsCommentServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Started CarBids Comment service on port 8099");
		SpringApplication.run(CarBidsCommentServiceApplication.class, args);
	}

}
