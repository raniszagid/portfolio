package com.example.carservice.dao;

import com.example.carservice.dto.CarDTO;
import com.example.carservice.dto.MasterDTO;
import com.example.carservice.dto.ServiceDTO;
import com.example.carservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class WorkJoinDAO {
    private final JdbcTemplate jdbc;
    @Autowired
    public WorkJoinDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public List<WorkJoin> index() {
        return jdbc.query("SELECT w.id AS id, w.date_work as work_date, m.name AS master_name," +
                        " s.name AS service_name, c.num AS car_num, c.mark AS car_mark," +
                        " c.color AS car_color FROM works w JOIN masters m" +
                        " ON w.master_id = m.id JOIN services s ON w.service_id = s.id" +
                        " JOIN cars c ON w.car_id = c.id;",
                new BeanPropertyRowMapper<>(WorkJoin.class));
    }

    public WorkJoin show(int id) {
        return jdbc.query("SELECT w.id AS id, w.date_work as work_date, m.name AS master_name," +
                                " s.name AS service_name, c.num AS car_num, c.mark AS car_mark," +
                                " c.color AS car_color FROM works w JOIN masters m" +
                                " ON w.master_id = m.id JOIN services s ON w.service_id = s.id" +
                                " JOIN cars c ON w.car_id = c.id where w.id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(WorkJoin.class))
                .stream().findAny().orElse(null);
    }

    public void insert(Work w, CarDTO c, MasterDTO m, ServiceDTO s) {
        w.setCarId(c.getCarId());
        w.setMasterId(m.getMasterId());
        w.setServiceId(s.getServiceId());
        jdbc.update("INSERT INTO works(date_work, master_id, car_id, service_id)" +
                        " VALUES(?,?,?,?)", w.getDateWork(), w.getMasterId(),
                w.getCarId(), w.getServiceId());
    }

    public void update(int id, WorkJoin wj, CarDTO c, MasterDTO m, ServiceDTO s) {
        Work w = new Work();
        w.setDateWork(wj.getWorkDate());
        w.setCarId(c.getCarId());
        w.setMasterId(m.getMasterId());
        w.setServiceId(s.getServiceId());
        jdbc.update("UPDATE works SET date_work=?, master_id=?, car_id=?," +
                        " service_id=?  WHERE id=?", w.getDateWork(), w.getMasterId(),
                w.getCarId(), w.getServiceId(), id);
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM works WHERE id=?", id);
    }
}
