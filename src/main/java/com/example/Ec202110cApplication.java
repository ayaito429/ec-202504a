package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
public class Ec202110cApplication {

	public static void main(String[] args) {
		SpringApplication.run(Ec202110cApplication.class, args);
	}

}
