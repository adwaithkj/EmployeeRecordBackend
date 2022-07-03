package com.oracle.employeerecord.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.sym.Name;
import com.oracle.employeerecord.model.Employee;
import com.oracle.employeerecord.payload.LoginReq;
import com.oracle.employeerecord.repo.EmpRepo;

@Controller
@RequestMapping("/employees")
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

    @RequestMapping("/login")
    public String login() {

        return "index.html";
    }

    @RequestMapping("/login/auth")
    @ResponseBody
    public String auth(@RequestBody LoginReq loginReq) {
        System.out.println(loginReq.getUsername() + " " + loginReq.getPassword());

        return "auth successful";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Employee> getAllEmp() {
        return empRepo.findAll();

    }

}
