package com.example.carservice.dao;

import com.example.carservice.model.Master;
import com.example.carservice.model.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ServiceDAO {
    private final JdbcTemplate jdbc;
    @Autowired
    public ServiceDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public List<Service> index() {
        return jdbc.query("SELECT * FROM services",
                new BeanPropertyRowMapper<>(Service.class));
    }
    public Service show(int id) {
        return jdbc.query("SELECT * FROM services WHERE id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Service.class))
                .stream().findAny().orElse(null);
    }
    public void insert(Service service) {
        jdbc.update("INSERT INTO services(name, cost_our, cost_foreign) VALUES(?,?,?)",
                service.getName(), service.getCostOur(), service.getCostForeign());
    }

    public void update(int id, Service service) {
        jdbc.update("UPDATE services SET name=?, cost_our=?, cost_foreign=?  WHERE id=?",
                service.getName(), service.getCostOur(), service.getCostForeign(), id);
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM services WHERE id=?", id);
    }
}
