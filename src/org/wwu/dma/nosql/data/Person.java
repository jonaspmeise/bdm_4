package org.wwu.dma.nosql.data;

import java.util.Date;

public class Person {
    private Date birthDate;
    private String firstName;
    private String lastName;

    public Person(Date birthDate, String firstName, String lastName) {
        this.birthDate = birthDate;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
