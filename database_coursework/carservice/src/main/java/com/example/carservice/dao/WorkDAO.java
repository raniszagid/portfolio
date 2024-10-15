package com.example.carservice.dao;


import com.example.carservice.model.CostResult;
import com.example.carservice.model.Master;
import com.example.carservice.model.MasterResult;
import com.example.carservice.model.Work;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class WorkDAO {
    private final JdbcTemplate jdbc;
    @Autowired
    public WorkDAO(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }
    public List<Work> index() {
        return jdbc.query("SELECT * FROM works",
                new BeanPropertyRowMapper<>(Work.class));
    }
    public Work show(int id) {
        return jdbc.query("SELECT * FROM works WHERE id=?",
                        new Object[]{id}, new BeanPropertyRowMapper<>(Work.class))
                .stream().findAny().orElse(null);
    }
    public void insert(Work w) {
        jdbc.update("INSERT INTO works(date_work, master_id, car_id, service_id)" +
                        " VALUES(?,?,?,?)", w.getDateWork(), w.getMasterId(),
                w.getCarId(), w.getServiceId());
    }

    public void update(int id, Work w) {
        jdbc.update("UPDATE works SET date_work=?, master_id=?, car_id=?," +
                        " service_id=?  WHERE id=?", w.getDateWork(), w.getMasterId(),
                w.getCarId(), w.getServiceId(), id);
    }

    public void delete(int id) {
        jdbc.update("DELETE FROM works WHERE id=?", id);
    }

    public CostResult countCost(LocalDate start, LocalDate end) {
        float foreign = jdbc.queryForObject("SELECT coalesce(SUM(CASE" +
                " WHEN c.is_foreign THEN s.cost_foreign" +
                " ELSE 0" +
                " END" +
                " ), 0) AS total_cost" +
                " FROM works w" +
                " JOIN cars c ON w.car_id = c.id" +
                " JOIN services s ON w.service_id = s.id" +
                " WHERE w.date_work BETWEEN ? AND ?;",
                Float.class, start, end);
        float russian = jdbc.queryForObject("SELECT coalesce(SUM(CASE" +
                        " WHEN c.is_foreign THEN 0" +
                        " ELSE s.cost_our" +
                        " END" +
                        " ),0) AS total_cost" +
                        " FROM works w" +
                        " JOIN cars c ON w.car_id = c.id" +
                        " JOIN services s ON w.service_id = s.id" +
                        " WHERE w.date_work BETWEEN ? AND ?;",
                Float.class, start, end);
        return new CostResult(foreign, russian, foreign+russian);
    }

    public List<MasterResult> getTopFive(int month, int year) {
        return jdbc.query("SELECT m.name as master_name, COUNT(DISTINCT w.car_id) AS quantity " +
                        "FROM works w " +
                        "JOIN masters m ON w.master_id = m.id " +
                        "WHERE DATE_PART('month', w.date_work) = ?" +
                        " and DATE_PART('year', w.date_work) = ?" +
                        " GROUP BY m.name " +
                        "ORDER BY quantity DESC " +
                        "LIMIT 5;",
                new BeanPropertyRowMapper<>(MasterResult.class), month, year);
    }
}
