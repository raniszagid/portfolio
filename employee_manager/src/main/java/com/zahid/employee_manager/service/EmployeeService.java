package com.zahid.employee_manager.service;

import com.zahid.employee_manager.dao.EmployeeDAO;
import com.zahid.employee_manager.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class EmployeeService {
    private final EmployeeDAO employeeDAO;

    @Autowired
    public EmployeeService(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public String findById(int id) {
        Optional<Employee> employee = employeeDAO.findUserById(id);
        if (employee.isPresent()) {
            return employee.get().toString();
        }
        else {
            return "Сотрудника с данным id не обнаружено\n";
        }
    }

    public List<String> groupByName() {
        return employeeDAO.groupUsersByName();
    }

    public List<Employee> findBetween(int start, int end) {
        return employeeDAO.findBetweenDates(start, end);
    }
}
