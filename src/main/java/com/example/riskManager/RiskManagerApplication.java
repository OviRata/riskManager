package com.example.riskManager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RiskManagerApplication {
	public static void main(String[] args) {
		SpringApplication.run(RiskManagerApplication.class, args);
	}
}