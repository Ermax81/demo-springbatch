package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.item.validator.ValidationException;

@AllArgsConstructor
@Getter
@Setter
public class PersonItemProcessor implements ItemProcessor<Person,Person> {

    private static final Logger log = LoggerFactory.getLogger(PersonItemProcessor.class);

    private SpringValidator<Person> personValidator;

    @Override
    public Person process(Person person) throws Exception {
        Person transformedPerson = null;
        try {
            personValidator.validate(person);

            final String firstName = person.getFirstName().toUpperCase();
            final String lastName = person.getLastName().toUpperCase();
            final int age = person.getAge();

            transformedPerson = new Person(firstName, lastName, age);

            log.info("Converting (" + person + ") into (" + transformedPerson + ")");


        } catch(ValidationException v) {

            log.error("Error when validating Person "+v.getMessage());
        }

        return transformedPerson;
    }

}
