package ru.zahid.springcourse.sensor_rest.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.zahid.springcourse.sensor_rest.dto.MeasurementDTO;
import ru.zahid.springcourse.sensor_rest.models.Measurement;
import ru.zahid.springcourse.sensor_rest.services.MeasurementService;
import ru.zahid.springcourse.sensor_rest.services.SensorService;

@Component
public class MeasurementValidator implements Validator {
    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return false;
    }

    @Override
    public void validate(Object target, Errors errors) {
        Measurement measurement = (Measurement) target;
        if (measurement.getSensor() == null) return;
        if (!sensorService.findOneByName(measurement.getSensor().getName())) {
            errors.rejectValue("sensor", "", "There is no sensor with such name");
        }
    }
}
