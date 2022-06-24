package com.example.batchprocessing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchProcessingApplication {

	public static void main(String[] args) {

		//SpringApplication.run(BatchProcessingApplication.class, args);

		//SpringApplication.exit() and System.exit() ensure that the JVM exits upon job completion
		// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-application-exit
		System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));

	}

}
