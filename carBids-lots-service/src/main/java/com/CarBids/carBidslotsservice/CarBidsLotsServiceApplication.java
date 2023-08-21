package com.CarBids.carBidslotsservice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
@EnableScheduling
@EnableFeignClients
@EnableHystrix
public class CarBidsLotsServiceApplication {
	private static final Logger logger = LoggerFactory.getLogger(CarBidsLotsServiceApplication.class);

	public static void main(String[] args) {
		logger.info("Started CarBids lots service on port 8093"+" "+ LocalDateTime.now());
		SpringApplication.run(CarBidsLotsServiceApplication.class, args);
	}

}
