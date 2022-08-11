package com.example.batchprocessing.demo;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EntityScan(basePackages = "com.example.batchprocessing.demo")
public class BatchProcessingApplication {

	public static void main(String[] args) throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {

		// Initialize context without starting jobs
		ConfigurableApplicationContext ctx = SpringApplication.run(BatchProcessingApplication.class, args);

		JobLauncher jobLauncher = (JobLauncher) ctx.getBean("jobLauncher");
		Job jobD = (Job) ctx.getBean("jobMessageDoNotKnow");
		Job jobF = (Job) ctx.getBean("jobMessageFail");
		Job jobI = (Job) ctx.getBean("jobSharedInfo");

		jobLauncher.run(jobI, new JobParameters());
		jobLauncher.run(jobF, new JobParameters());
		jobLauncher.run(jobD, new JobParameters());

		//SpringApplication.exit() and System.exit() ensure that the JVM exits upon job completion
		// https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#boot-features-application-exit
		//System.exit(SpringApplication.exit(SpringApplication.run(BatchProcessingApplication.class, args)));

	}

}
