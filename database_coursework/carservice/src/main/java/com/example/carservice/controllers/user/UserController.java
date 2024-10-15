package com.example.carservice.controllers.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.carservice.dao.*;
import com.example.carservice.dao.MasterDAO;
import com.example.carservice.dao.ServiceDAO;

@Controller
@RequestMapping("/user")
public class UserController {
    private final CarDAO carDAO;
    private final MasterDAO masterDAO;
    private final ServiceDAO serviceDAO;
    @Autowired
    public UserController(CarDAO carDAO, MasterDAO masterDAO, ServiceDAO serviceDAO) {
        this.carDAO = carDAO;
        this.masterDAO = masterDAO;
        this.serviceDAO = serviceDAO;
    }

    @GetMapping("/cars")
    public String getCars(Model model) {
        model.addAttribute("cars", carDAO.index());
        return "view/user/cars/index";
    }

    @GetMapping("/masters")
    public String getMasters(Model model) {
        model.addAttribute("masters", masterDAO.index());
        return "view/user/masters/index";
    }

    @GetMapping("/services")
    public String getServices(Model model) {
        model.addAttribute("services", serviceDAO.index());
        return "view/user/services/index";
    }

    @GetMapping("")
    public String getMainPage() {
        return "view/user/main";
    }
}
