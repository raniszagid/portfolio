package ru.zahid.springcourse.sensor_rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class SensorDTO {
    @NotNull
    @Size(min = 2, max = 30, message = "Length of name should be > 2 and < 30")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
