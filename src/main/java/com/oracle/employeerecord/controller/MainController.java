package com.oracle.employeerecord.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.oracle.employeerecord.model.Employee;
import com.oracle.employeerecord.repo.EmpRepo;

@Controller
@RequestMapping(path = "/employees")
public class MainController {
    @Autowired
    private EmpRepo empRepo;

    @PostMapping(path = "/add")
    public @ResponseBody String addNewEmp(@RequestParam String name, @RequestParam Long id,
            @RequestParam String password) {

        Employee e = new Employee();
        e.setName(name);
        e.setId(id);
        e.setPassword(password);

        empRepo.save(e);

        return "saved";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Employee> getAllEmp() {
        return empRepo.findAll();

    }

}
