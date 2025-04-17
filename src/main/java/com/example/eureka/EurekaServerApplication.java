package com.example.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EurekaServerApplication {

	public static void main(String[] args) {

		System.out.println("Hello");
		SpringApplication.run(EurekaServerApplication.class, args);
	}

}
