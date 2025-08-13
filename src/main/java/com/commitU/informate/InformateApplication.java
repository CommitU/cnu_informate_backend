package com.commitU.informate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InformateApplication {

	public static void main(String[] args) {
		SpringApplication.run(InformateApplication.class, args);
	}

}
