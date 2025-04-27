package com.example.StudentApp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class StudentAppApplication {
	private static final Logger logger = LoggerFactory.getLogger(StudentAppApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(StudentAppApplication.class, args);
		logger.info("Spring Boot application started! Server is running...");
	}

}
