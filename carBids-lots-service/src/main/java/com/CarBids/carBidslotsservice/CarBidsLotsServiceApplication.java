package com.CarBids.carBidslotsservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CarBidsLotsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarBidsLotsServiceApplication.class, args);
	}

}
