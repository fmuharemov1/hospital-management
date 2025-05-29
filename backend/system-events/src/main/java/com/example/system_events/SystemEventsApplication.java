package com.example.system_events;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication

@ComponentScan(basePackages = {
		"com.example.system_events",
		"com.example.logging" // obavezno da vidi WebConfig iz logging-core
})
public class SystemEventsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SystemEventsApplication.class, args);
	}
}
