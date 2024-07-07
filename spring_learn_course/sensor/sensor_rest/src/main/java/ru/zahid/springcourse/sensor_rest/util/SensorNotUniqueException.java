package ru.zahid.springcourse.sensor_rest.util;

public class SensorNotUniqueException extends RuntimeException {
    public SensorNotUniqueException(String message) {
        super(message);
    }
}
