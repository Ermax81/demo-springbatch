package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing //adds many critical beans that support jobs and save you a lot of leg work
                       //provides a memory-based database
@AllArgsConstructor
public class BatchConfiguration {

    public JobBuilderFactory jobBuilderFactory;

    public StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job jobMessageDoNotKnow(JobCompletionNotificationListener listener) {
        // JOB_NAME in table BATCH_JOB_INSTANCE i
        // JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS... in table BATCH_JOB_EXECUTION e
        // i.JOB_INSTANCE_ID = e.JOB_INSTANCE_ID

        return jobBuilderFactory.get("jobMessageDoNotKnow")
                .incrementer(new RunIdIncrementer()) //need an incrementer, because jobs use a database to maintain execution state
                .listener(listener)
                .start(stepDoNotKnow())
                .build();

    }

    @Bean
    public Job jobMessageFail(JobCompletionNotificationListener listener) {
        // JOB_NAME in table BATCH_JOB_INSTANCE i
        // JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS... in table BATCH_JOB_EXECUTION e
        // i.JOB_INSTANCE_ID = e.JOB_INSTANCE_ID

        return jobBuilderFactory.get("jobMessageFail")
                .incrementer(new RunIdIncrementer()) //need an incrementer, because jobs use a database to maintain execution state
                .listener(listener)
                .start(stepDoNotKnow())
                .build();

    }

    @Bean
    public Tasklet failTasklet() {
        String message = "Failing to build something...";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepFail() {
        return stepBuilderFactory.get("stepFail")
                .allowStartIfComplete(true)
                .tasklet(failTasklet())
                .build();
    }

    @Bean
    public Tasklet doneTasklet() {
        String message = "I'm done";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepDone() {
        return stepBuilderFactory.get("stepDone")
                .allowStartIfComplete(true)
                .tasklet(doneTasklet())
                .build();
    }

    @Bean
    public Tasklet thinkTasklet() {
        String message = "<Thinking of ...>";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepThink() {
        return stepBuilderFactory.get("stepThink")
                .allowStartIfComplete(true)
                .tasklet(thinkTasklet())
                .build();
    }

    @Bean
    public Tasklet doNotKnowTasklet() {
        String message = "I do not know what to do";
        return new MessageTasklet(message, true);
    }

    @Bean
    public Step stepDoNotKnow() {
        return stepBuilderFactory.get("stepDoNotKnow")
                .allowStartIfComplete(true)
                .tasklet(doNotKnowTasklet())
                .build();
    }


    //////////////////////

    // Required to share Objets between tasklet, step, ...
    // Map<String,Object> : String = id to put/get object
    @Bean
    public ExecutionContext executionContext() {
        return new ExecutionContext();
    }

    @Bean
    public Tasklet createSharedInfo() {
        return new CreateInfoTasklet();
    }

    @Bean
    public Tasklet getSharedInfo() {
        return new GetInfoTasklet();
    }

    @Bean
    public Step stepCreateSharedInfo() {
        return stepBuilderFactory.get("stepSharedInfo")
                .tasklet(createSharedInfo())
                .build();
    }

    @Bean
    public Step stepGetSharedInfo() {
        return stepBuilderFactory.get("stepSharedInfo")
                .tasklet(getSharedInfo())
                .build();
    }

    @Bean
    public Job jobSharedInfo(JobCompletionNotificationListener listener) {

        // JOB_NAME in table BATCH_JOB_INSTANCE i
        // JOB_EXECUTION_ID, START_TIME, END_TIME, STATUS... in table BATCH_JOB_EXECUTION e
        // i.JOB_INSTANCE_ID = e.JOB_INSTANCE_ID
        return jobBuilderFactory.get("shareInfo")
                .incrementer(new RunIdIncrementer()) //need an incrementer, because jobs use a database to maintain execution state
                .listener(listener)
                .start(stepCreateSharedInfo())
                .next(stepGetSharedInfo())
                .build();
    }

}
