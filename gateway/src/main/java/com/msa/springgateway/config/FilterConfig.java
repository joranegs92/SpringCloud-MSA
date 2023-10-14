package com.msa.springgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class FilterConfig {
/*
	//@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder){
		return builder.routes()
				.route(r -> r.path("/first-service/**")//
						.filters(f ->f.addRequestHeader("first-request","first-request-header")//헤더에 정보 전달
						.addResponseHeader("first-response", "first-response-header"))
						.uri("http://localhost:8081"))
				.route(r -> r.path("/second-service/**")
						.filters(f ->f.addRequestHeader("second-request","second-request-header")
								.addResponseHeader("second-response", "second-response-header"))
						.uri("http://localhost:8082"))
				.build();
	}*/
}
