package ru.zahid.springcourse.sensor_rest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zahid.springcourse.sensor_rest.dto.SensorDTO;
import ru.zahid.springcourse.sensor_rest.models.Sensor;
import ru.zahid.springcourse.sensor_rest.services.SensorService;

@Component
public class SensorValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Sensor sensor = (Sensor) target;
        if (sensorService.findOneByName(sensor.getName()))
            errors.rejectValue("name", "", "This name is already taken for another sensor");
    }
}
