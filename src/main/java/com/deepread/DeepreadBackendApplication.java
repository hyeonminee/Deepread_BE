package com.deepread;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DeepreadBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeepreadBackendApplication.class, args);
	}

}
