package com.example.carservice.controllers.admin;

import com.example.carservice.dao.*;
import com.example.carservice.dto.CarDTO;
import com.example.carservice.dto.MasterDTO;
import com.example.carservice.dto.ServiceDTO;
import com.example.carservice.model.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final CarDAO carDAO;
    private final MasterDAO masterDAO;
    private final ServiceDAO serviceDAO;
    private final WorkDAO workDAO;
    private final WorkJoinDAO workJoinDAO;

    @Autowired
    public AdminController(CarDAO carDAO, MasterDAO masterDAO, ServiceDAO serviceDAO, WorkDAO workDAO, WorkJoinDAO workJoinDAO) {
        this.carDAO = carDAO;
        this.masterDAO = masterDAO;
        this.serviceDAO = serviceDAO;
        this.workDAO = workDAO;
        this.workJoinDAO = workJoinDAO;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/cars")
    public String getCars(Model model) {
        model.addAttribute("cars", carDAO.index());
        return "view/admin/cars/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/cars/{id}")
    public String showCar(@PathVariable("id") int id, Model model) {
        model.addAttribute("car", carDAO.show(id));
        return "view/admin/cars/show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/cars/new")
    public String newCar(Model model) {
        model.addAttribute("car", new Car());
        return "view/admin/cars/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/cars")
    public String createCar(@ModelAttribute("car") @Valid Car car,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "view/admin/cars/new";
        carDAO.insert(car);
        return "redirect:/admin/cars";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/cars/{id}/edit")
    public String editCar(Model model, @PathVariable("id") int id) {
        model.addAttribute("car", carDAO.show(id));
        return "view/admin/cars/edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/cars/{id}")
    public String updateCar(@ModelAttribute("car") @Valid Car car,
                         BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "view/admin/cars/edit";
        carDAO.update(id, car);
        return "redirect:/admin/cars";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/cars/{id}")
    public String deleteCar(@PathVariable("id") int id) {
        carDAO.delete(id);
        return "redirect:/admin/cars";
    }




    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/masters")
    public String getMasters(Model model) {
        model.addAttribute("masters", masterDAO.index());
        return "view/admin/masters/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/masters/{id}")
    public String showMaster(@PathVariable("id") int id, Model model) {
        model.addAttribute("master", masterDAO.show(id));
        return "view/admin/masters/show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/masters/new")
    public String newMaster(Model model) {
        model.addAttribute("master", new Master());
        return "view/admin/masters/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/masters")
    public String createMaster(@ModelAttribute("master") @Valid Master master,
                            BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "view/admin/masters/new";
        masterDAO.insert(master);
        return "redirect:/admin/masters";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/masters/{id}/edit")
    public String editMaster(Model model, @PathVariable("id") int id) {
        model.addAttribute("master", masterDAO.show(id));
        return "view/admin/masters/edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/masters/{id}")
    public String updateMaster(@ModelAttribute("master") @Valid Master master,
                            BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "view/admin/masters/edit";
        masterDAO.update(id, master);
        return "redirect:/admin/masters";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/masters/{id}")
    public String deleteMaster(@PathVariable("id") int id) {
        masterDAO.delete(id);
        return "redirect:/admin/masters";
    }




    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/services")
    public String getServices(Model model) {
        model.addAttribute("services", serviceDAO.index());
        return "view/admin/services/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/services/{id}")
    public String showService(@PathVariable("id") int id, Model model) {
        model.addAttribute("service", serviceDAO.show(id));
        return "view/admin/services/show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/services/new")
    public String newService(Model model) {
        model.addAttribute("service", new Service());
        return "view/admin/services/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/services")
    public String createService(@ModelAttribute("service") @Valid Service service,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "view/admin/services/new";
        serviceDAO.insert(service);
        return "redirect:/admin/services";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/services/{id}/edit")
    public String editService(Model model, @PathVariable("id") int id) {
        model.addAttribute("service", serviceDAO.show(id));
        return "view/admin/services/edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/services/{id}")
    public String updateService(@ModelAttribute("service") @Valid Service service,
                               BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "view/admin/services/edit";
        serviceDAO.update(id, service);
        return "redirect:/admin/services";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/services/{id}")
    public String deleteService(@PathVariable("id") int id) {
        serviceDAO.delete(id);
        return "redirect:/admin/services";
    }




    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public String getMainPage() {
        return "view/admin/main";
    }



    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/works")
    public String getWorks(Model model) {
        model.addAttribute("works", workJoinDAO.index());
        return "view/admin/works/index";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/works/{id}")
    public String showWork(@PathVariable("id") int id, Model model) {
        model.addAttribute("work", workJoinDAO.show(id));
        return "view/admin/works/show";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/works/new")
    public String newWork(Model model) {
        model.addAttribute("work", new Work());
        model.addAttribute("masters", masterDAO.index());
        model.addAttribute("master", new MasterDTO());
        model.addAttribute("services", serviceDAO.index());
        model.addAttribute("service", new ServiceDTO());
        model.addAttribute("cars", carDAO.index());
        model.addAttribute("car", new CarDTO());
        return "view/admin/works/new";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/works")
    public String createWork(@ModelAttribute("work") @Valid Work work,
                             @ModelAttribute("car") @Valid CarDTO car,
                             @ModelAttribute("master") @Valid MasterDTO master,
                             @ModelAttribute("service") @Valid ServiceDTO service,
                                BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            return "view/admin/works/new";
        workJoinDAO.insert(work, car, master, service);
        return "redirect:/admin/works";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/works/{id}/edit")
    public String editWork(Model model, @PathVariable("id") int id) {
        model.addAttribute("work", workJoinDAO.show(id));
        model.addAttribute("masters", masterDAO.index());
        model.addAttribute("master", new MasterDTO());
        model.addAttribute("services", serviceDAO.index());
        model.addAttribute("service", new ServiceDTO());
        model.addAttribute("cars", carDAO.index());
        model.addAttribute("car", new CarDTO());
        return "view/admin/works/edit";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/works/{id}")
    public String updateWork(@ModelAttribute("work") @Valid WorkJoin work,
                                @ModelAttribute("car") @Valid CarDTO car,
                                @ModelAttribute("master") @Valid MasterDTO master,
                                @ModelAttribute("service") @Valid ServiceDTO service,
                                BindingResult bindingResult, @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) return "view/admin/works/edit";
        workJoinDAO.update(id, work, car, master, service);
        return "redirect:/admin/works";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/works/{id}")
    public String deleteWork(@PathVariable("id") int id) {
        workJoinDAO.delete(id);
        return "redirect:/admin/works";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/statistic/cost")
    public String seeCost() {
        return "view/admin/statistic/cost_date";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/statistic/cost")
    public String countCost(Model model, @RequestParam("start") @Valid LocalDate start,
                            @RequestParam("end") @Valid LocalDate end) {
        model.addAttribute("result", workDAO.countCost(start, end));
        return "view/admin/statistic/cost_date";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/statistic/best")
    public String seeTopFiveMonth() {
        return "view/admin/statistic/best_masters";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/statistic/best")
    public String countTopFiveMonth(Model model, @RequestParam("month") @Valid int m,
                            @RequestParam("year") @Valid int y) {
        model.addAttribute("result", workDAO.getTopFive(m, y));
        return "view/admin/statistic/best_masters";
    }
}
