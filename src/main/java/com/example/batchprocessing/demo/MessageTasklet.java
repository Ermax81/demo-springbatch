package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

import java.security.SecureRandom;

@Data
@AllArgsConstructor
public class MessageTasklet implements Tasklet {

    private static final Logger logger = LoggerFactory.getLogger(MessageTasklet.class);

    private String message;
    private boolean isCompleted;

    @Override
    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {

        // return null; //default

        SecureRandom secureRandom = new SecureRandom();
        boolean randomBoolean = secureRandom.nextBoolean();
        int randomInt = secureRandom.nextInt();
        ExitStatus status = ExitStatus.COMPLETED;

        if (isCompleted || randomBoolean) {
            logger.info(message+": Status="+status.toString());
        } else {
            if (randomInt % 2 == 0) {
                // even number
                status = ExitStatus.NOOP;
                logger.warn(message+": Status="+status.toString());
            } else {
                // odd number
                status = ExitStatus.FAILED;
                logger.error(message+": Status="+status.toString());
            }
        }
        stepContribution.setExitStatus(status);
        return RepeatStatus.FINISHED;
    }
}
