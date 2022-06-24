package com.example.batchprocessing.demo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    private String firstName;
    private String lastName;

    public String toString() {
        return "firstName: " + firstName + ", lastName: " + lastName;
    }

}
