package ru.zahid.springcourse.sensor_rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zahid.springcourse.sensor_rest.models.Sensor;
import ru.zahid.springcourse.sensor_rest.repositories.SensorRepository;

@Service
@Transactional(readOnly = true)
public class SensorService {
    private final SensorRepository sensorRepository;

    @Autowired
    public SensorService(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    public boolean findOneByName(String name) {
        return sensorRepository.findByName(name).isPresent();
    }

    @Transactional
    public void save(Sensor sensor) {
        sensorRepository.save(sensor);
    }
}
