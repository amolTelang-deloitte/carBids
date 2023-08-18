package com.CarBids.carBidsbiddingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CarBidsBiddingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarBidsBiddingServiceApplication.class, args);
	}

}
