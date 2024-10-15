package ru.zahid.springcourse.sensor_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zahid.springcourse.sensor_rest.models.Sensor;

import java.util.Optional;

@Repository
public interface SensorRepository  extends JpaRepository<Sensor, Integer> {
    public Optional<Sensor> findByName(String name);
}
