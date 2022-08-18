package com.example.batchprocessing.demo;

import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.batchprocessing.demo")
public class BatchProcessingApplication {

	public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

		//SpringApplication.exit() and System.exit() ensure that the JVM exits upon job completion
		// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-application-exit
		System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));

	}

}
