package com.example.batchprocessing.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.ItemProcessListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class StepItemProcessListener implements ItemProcessListener<Person, Person> {

    @Override
    public void beforeProcess(Person person) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP_ITEM_PROCESSOR_LISTENER beforeProcess");
        if (person!=null) {
            sb.append(" Person=[");
            sb.append(person.toString());
            sb.append("]");
        }
        log.info(sb.toString());
    }

    @Override
    public void afterProcess(Person person, Person person2) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP_ITEM_PROCESSOR_LISTENER afterProcess");
        if (person!=null) {
            sb.append(" (1)Person=[");
            sb.append(person.toString());
            sb.append("]");
        }
        if (person2!=null) {
            sb.append(" (2)Person=[");
            sb.append(person2.toString());
            sb.append("]");
        }
        log.info(sb.toString());
    }

    @Override
    public void onProcessError(Person person, Exception e) {
        StringBuilder sb = new StringBuilder();
        sb.append("STEP_ITEM_PROCESSOR_LISTENER onProcessError");
        if (person!=null) {
            sb.append(" Person=[");
            sb.append(person.toString());
            sb.append("]");
        }
        sb.append(" Error=[");
        sb.append(e.getMessage());
        sb.append("]");
        log.error(sb.toString());
    }
}
