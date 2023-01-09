package com.example.batchprocessing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class StepItemWriteListener implements ItemWriteListener<Person> {

    @Override
    public void beforeWrite(List<? extends Person> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP_ITEM_WRITE_LISTENER beforeWrite");
        if (!list.isEmpty()) {
            sb.append(" nbPerson:");
            sb.append(list.size());
        }
        log.info(sb.toString());
    }

    @Override
    public void afterWrite(List<? extends Person> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP_ITEM_WRITE_LISTENER afterWrite");
        if (!list.isEmpty()) {
            sb.append(" nbPerson:");
            sb.append(list.size());
        }
        log.info(sb.toString());
    }

    @Override
    public void onWriteError(Exception e, List<? extends Person> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP_ITEM_WRITER_LISTENER onWriteError");
        sb.append(" Error=[");
        sb.append(e.getMessage());
        sb.append("]");
        log.error(sb.toString());
    }
}
