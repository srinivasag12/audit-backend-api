package com.api.central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CentralAuditApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CentralAuditApiApplication.class, args);
	}
}
