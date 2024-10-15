package com.zahid.employee_manager.dao;

import com.zahid.employee_manager.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EmployeeDAO {
    private final JdbcTemplate jdbc;

    @Autowired
    public EmployeeDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Employee> findUserById(int id) {
        return jdbc
                .query("SELECT * FROM employees WHERE id=?",
                        new BeanPropertyRowMapper<>(Employee.class), id)
                .stream().findAny();
    }

    public List<String> groupUsersByName() {
        return jdbc.queryForList("SELECT name FROM employees GROUP BY name", String.class);
    }

    public List<Employee> findBetweenDates(int start, int end) {
        return jdbc.query("SELECT * FROM employees WHERE DATE_PART('year', birth_date) BETWEEN ? AND ?",
                        new BeanPropertyRowMapper<>(Employee.class), start, end);
    }
}
