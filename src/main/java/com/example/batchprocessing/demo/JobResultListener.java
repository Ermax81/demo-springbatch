package com.example.batchprocessing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JobResultListener implements JobExecutionListener {

    @Override
    public void beforeJob(JobExecution jobExecution) {
        Date createTime = jobExecution.getCreateTime();
        Long id = jobExecution.getJobId();
        log.info("JOB_LISTENER jobId:["+id+"] createTime:["+createTime+"]");
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        Date startTime = jobExecution.getStartTime();
        Date endTime = jobExecution.getEndTime();
        Long id = jobExecution.getJobId();
        if (jobExecution.getStatus() == BatchStatus.COMPLETED ) {
            //job success
            log.info("JOB_LISTENER jobId:["+id+"] SUCCESS startTime:["+startTime+"] endTime:["+endTime+"]");
        }
        else if (jobExecution.getStatus() == BatchStatus.FAILED) {
            //job failure
            log.info("JOB_LISTENER jobId:["+id+"] FAILED startTime:["+startTime+"] endTime:["+endTime+"]");
        }
        log.info("JOB_LISTENER jobId:["+id+"] status:["+jobExecution.getStatus()+"] startTime:["+startTime+"] endTime:["+endTime+"]");
    }
}
