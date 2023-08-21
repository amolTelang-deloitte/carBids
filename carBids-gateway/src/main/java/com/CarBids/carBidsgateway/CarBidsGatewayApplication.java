package com.CarBids.carBidsgateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionLocator;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@EnableDiscoveryClient
public class CarBidsGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarBidsGatewayApplication.class, args);

	}
	@Autowired
	RouteDefinitionLocator locator;

}
