package ru.zahid.springcourse.models;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public class Person {
    private int id;
    @NotEmpty(message = "Name cannot be empty")
    private String name;
    private int birthYear;

    public Person() {}

    public Person(int id, String name, int birthYear) {
        this.id = id;
        this.name = name;
        this.birthYear = birthYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(int birthYear) {
        this.birthYear = birthYear;
    }
}
