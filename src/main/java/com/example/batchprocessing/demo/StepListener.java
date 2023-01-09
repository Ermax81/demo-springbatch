package com.example.batchprocessing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.listener.StepExecutionListenerSupport;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class StepListener extends StepExecutionListenerSupport {

    @Override
    public void beforeStep(StepExecution stepExecution) {
        Date startTime = stepExecution.getStartTime();
        String stepName = stepExecution.getStepName();
        log.info("STEP "+stepName+" beforeStep startTime:"+startTime);
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        String stepName = stepExecution.getStepName();
        Date endTime = stepExecution.getEndTime();
        ExitStatus result = stepExecution.getExitStatus();
        log.info("STEP "+stepName+" afterStep ExitStatus:"+result+" endTime:"+endTime);
        return result;
    }
}

