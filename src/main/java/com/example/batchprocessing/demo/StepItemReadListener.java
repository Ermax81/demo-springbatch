package com.example.batchprocessing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StepItemReadListener implements ItemReadListener<Person> {

    @Override
    public void beforeRead() {
        log.info("STEP_ITEM_READ_LISTENER beforeRead");
    }

    @Override
    public void afterRead(Person person) {
        log.info("STEP_ITEM_READ_LISTENER afterRead Person=["+person.toString()+"]");
    }

    @Override
    public void onReadError(Exception e) {
        log.error("STEP_ITEM_READ_LISTENER "+e.getMessage());
    }
}
