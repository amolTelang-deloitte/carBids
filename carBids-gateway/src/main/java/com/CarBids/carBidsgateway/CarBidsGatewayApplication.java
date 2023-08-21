package com.CarBids.carBidsgateway;

import com.CarBids.carBidsgateway.filter.AuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

	private final static Logger logger = LoggerFactory.getLogger(CarBidsGatewayApplication.class);
	public static void main(String[] args) {
		SpringApplication.run(CarBidsGatewayApplication.class, args);
		logger.info("Starting Car Bids gateway application on port 8090");

	}
	@Autowired
	RouteDefinitionLocator locator;

}
