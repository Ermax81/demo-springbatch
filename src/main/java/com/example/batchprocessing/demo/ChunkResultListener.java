package com.example.batchprocessing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ChunkResultListener implements ChunkListener {

    @Override
    public void beforeChunk(ChunkContext chunkContext) {
        String stepName = chunkContext.getStepContext().getStepName();
        String id = chunkContext.getStepContext().getId();
        log.info("STEP "+stepName+" beforeChunk id:"+id);
    }

    @Override
    public void afterChunk(ChunkContext chunkContext) {
        String stepName = chunkContext.getStepContext().getStepName();
        String id = chunkContext.getStepContext().getId();
        boolean isComplete = chunkContext.isComplete();
        log.info("STEP "+stepName+" afterChunk id:"+id+" isComplete"+isComplete);
    }

    @Override
    public void afterChunkError(ChunkContext chunkContext) {
        log.error(chunkContext.toString());
    }
}
