package ru.zahid.springcourse.FirstRestApp.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class FirstRESTController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello world";
    }
}
