package com.shubheksha;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ShubhekshaApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShubhekshaApiApplication.class, args);
	}

}
