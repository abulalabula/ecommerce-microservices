package com.ecommerce.account_service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AccountServiceApplication {

//	@Value("${server.port}")
//	private String serverPort;

	public static void main(String[] args) {
//		System.out.println("Starting Account Service on port: " + System.getProperty("server.port"));

		SpringApplication.run(AccountServiceApplication.class, args);
	}

//	@Override
//	public void run(String... args) {
//		System.out.println("Spring Boot is running on port: " + serverPort);
//	}

}
