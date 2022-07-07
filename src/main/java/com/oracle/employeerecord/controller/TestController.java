package com.oracle.employeerecord.controller;

import org.springframework.stereotype.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseBody;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/api/test")
public class TestController {
    @GetMapping("/all")
    public @ResponseBody String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/employee")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public @ResponseBody String userAccess() {
        System.out.println("employee page reached");
        return "User Content.";
    }

    @GetMapping("/manager")
    @PreAuthorize("hasRole('MANAGER')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}
