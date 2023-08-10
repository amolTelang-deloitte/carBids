package com.CarBids.carBidsserviceregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class CarBidsServiceRegistryApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarBidsServiceRegistryApplication.class, args);
	}

}
