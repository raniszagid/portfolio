package com.zahid.employee_manager.launcher;

import com.zahid.employee_manager.model.Employee;
import com.zahid.employee_manager.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class EmployeeCommandLineRunner implements CommandLineRunner {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeCommandLineRunner(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }


    @Override
    public void run(String... args) {
        Scanner scanner = new Scanner(System.in);
        boolean isEnd = false;
        String menu = """
                *********
                Для поиска сотрудника по ID введите следующую команду
                findById [ID]
                Например:
                findById 2
                Для получения списка сгруппированных имён сотрудников введите следующую команду
                groupByName
                Для получения списка сотрудников, чьи даты рождения находятся в заданном интервале, введите следующую команду
                findBetween [год начала интервала] [год конца интервала]
                Например:
                findBetween 1990 1992
                Для завершения программы введите следующую команду
                exit
                *********
                """;
        System.out.println(menu);
        while (!isEnd) {
            String line = scanner.nextLine();
            String[] strings = line.split(" ");
            switch (strings[0]) {
                case "findById" -> {
                    try {
                        int id = Integer.parseInt(strings[1]);
                        System.out.println(employeeService.findById(id));
                    } catch (NumberFormatException e) {
                        System.out.println("ID введён некорректно");
                    }
                }
                case "groupByName" -> employeeService.groupByName().forEach(System.out::println);
                case "findBetween" -> {
                    try {
                        int start = Integer.parseInt(strings[1]);
                        int end = Integer.parseInt(strings[2]);
                        List<Employee> list = employeeService.findBetween(start, end);
                        if (list.isEmpty()) {
                            System.out.println("Сотрудников, родившихся в данном интервале, не найдено");
                        } else {
                            list.forEach(System.out::println);
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Год введён некорректно");
                    }
                }
                case "exit" -> isEnd = true;
                default -> System.out.println("Команда не найдена");
            }
        }
        System.out.println("Завершение работы...");
    }
}
