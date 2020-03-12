package com.interview.ceg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrazyEventGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrazyEventGatewayApplication.class, args);
	}

}