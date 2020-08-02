package com.iAnalyze.apiGateway.iAnalyzeApiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@EnableZuulProxy
@EnableEurekaClient
@SpringBootApplication
public class IAnalyzeApiGatewayApplication {

	@Bean
	public ZuulLoggingFilter ZuulLoggingFilter(){
		return new ZuulLoggingFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(IAnalyzeApiGatewayApplication.class, args);
	}

}
