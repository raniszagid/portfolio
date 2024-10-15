package com.example.carservice.dao;

import com.example.carservice.model.Car;
import com.example.carservice.model.Master;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MasterDAO {
    private final JdbcTemplate jdbc;
    @Autowired
    public MasterDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public List<Master> index() {
        return jdbc.query("SELECT * FROM masters",
                new BeanPropertyRowMapper<>(Master.class));
    }
    public Master show(int id) {
        return jdbc.query("SELECT * FROM masters WHERE id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Master.class))
                .stream().findAny().orElse(null);
    }
    public void insert(Master master) {
        jdbc.update("INSERT INTO masters(name) VALUES(?)",
                master.getName());
    }

    public void update(int id, Master master) {
        jdbc.update("UPDATE masters SET name=?  WHERE id=?",
                master.getName(), id);
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM masters WHERE id=?", id);
    }

}
