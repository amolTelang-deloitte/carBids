package com.CarBids.carBidsconfigserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigServer
public class CarBidsConfigServerApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarBidsConfigServerApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CarBidsConfigServerApplication.class, args);
		logger.info("Starting CarBids Config server on port 8888" + LocalDateTime.now());
	}

}
