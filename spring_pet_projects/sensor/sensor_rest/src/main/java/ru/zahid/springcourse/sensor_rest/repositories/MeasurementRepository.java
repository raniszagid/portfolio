package ru.zahid.springcourse.sensor_rest.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.zahid.springcourse.sensor_rest.models.Measurement;

@Repository
public interface MeasurementRepository  extends JpaRepository<Measurement, Integer>  {
}
