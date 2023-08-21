package com.CarBids.carBidsserviceregistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableEurekaServer
public class CarBidsServiceRegistryApplication {

	private static final Logger logger = LoggerFactory.getLogger(CarBidsServiceRegistryApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(CarBidsServiceRegistryApplication.class, args);
		logger.info("Starting CarBids registry server on port 8765" + LocalDateTime.now());
	}

}
