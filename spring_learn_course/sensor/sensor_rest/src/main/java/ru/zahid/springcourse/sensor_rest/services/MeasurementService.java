package ru.zahid.springcourse.sensor_rest.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.zahid.springcourse.sensor_rest.models.Measurement;
import ru.zahid.springcourse.sensor_rest.repositories.MeasurementRepository;
import ru.zahid.springcourse.sensor_rest.repositories.SensorRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class MeasurementService {
    private final MeasurementRepository measurementRepository;
    private final SensorRepository sensorRepository;

    @Autowired
    public MeasurementService(MeasurementRepository measurementRepository, SensorRepository sensorRepository) {
        this.measurementRepository = measurementRepository;
        this.sensorRepository = sensorRepository;
    }

    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Transactional
    public void save(Measurement measurement) {
        enrich(measurement);
        measurementRepository.save(measurement);
    }

    public void enrich(Measurement measurement) {
        measurement.setSensor(sensorRepository.findByName(measurement.getSensor().getName()).get());
        measurement.setDateTime(LocalDateTime.now());
    }

}
