package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @NotEmpty(message = "firstName must be not empty and should be in uppercase")
    private String firstName;

    private String lastName;

    private int age;

    public String toString() {
        return "firstName: " + firstName + ", lastName: " + lastName + ", age: " + age ;
    }

}
