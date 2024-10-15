package com.example.carservice.dao;

import com.example.carservice.model.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class CarDAO {
    private final JdbcTemplate jdbc;
    @Autowired
    public CarDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public List<Car> index() {
        return jdbc.query("SELECT * FROM cars",
                new BeanPropertyRowMapper<>(Car.class));
    }
    public Car show(int id) {
        return jdbc.query("SELECT * FROM cars WHERE id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Car.class))
                .stream().findAny().orElse(null);
    }
    public void insert(Car car) {
        jdbc.update("INSERT INTO cars(num, mark, color, is_foreign) VALUES(?,?,?,?)",
                car.getNum(), car.getMark(), car.getColor(), car.getIsForeign());
    }

    public void update(int id, Car car) {
        jdbc.update("UPDATE cars SET num=?, mark=?, color=?, is_foreign=?  WHERE id=?",
                car.getNum(), car.getMark(), car.getColor(), car.getIsForeign(), id);
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM cars WHERE id=?", id);
    }
}
