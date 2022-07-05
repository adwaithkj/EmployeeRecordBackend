package com.oracle.employeerecord.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.core.sym.Name;
import com.oracle.employeerecord.model.ERole;
import com.oracle.employeerecord.model.Employee;
import com.oracle.employeerecord.model.Role;
import com.oracle.employeerecord.payload.LoginReq;
import com.oracle.employeerecord.payload.SignupReq;
import com.oracle.employeerecord.repo.EmpRepo;
import com.oracle.employeerecord.repo.RoleRepo;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/employees")
public class MainController {
    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private RoleRepo roleRepo;

    // @Autowired
    // AuthenticationManager authenticationManager;

    // @Autowired
    // PasswordEncoder encoder;

    // @Autowired
    // JwtUtils jwtUtils;

    @RequestMapping(path = "/addrole")
    public @ResponseBody String setrole() {

        System.out.println(empRepo.findByUsername("adwaith").get().getPassword());

        return "set";

    }

    @PostMapping(path = "/signup")
    public @ResponseBody String signup(@RequestBody SignupReq signupReq) {

        if (empRepo.existsByUsername(signupReq.getUsername())) {
            return "the user already exists";
        }
        Employee e = new Employee();
        e.setName(signupReq.getName());
        e.setPassword(signupReq.getPassword());
        e.setUsername(signupReq.getUsername());
        // System.out.println(signupReq.getRole());

        Set<String> strRole = signupReq.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRole == null) { // assigns default role employee if no role is set
            Role empRole = roleRepo.findByName(ERole.EMPLOYEE).get();
            System.out.println(empRole.getName());
            roles.add(empRole);
        } else {
            strRole.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepo.findByName(ERole.ADMIN).get();
                        roles.add(adminRole);

                    case "manager":
                        Role managerRole = roleRepo.findByName(ERole.MANAGER).get();
                        roles.add(managerRole);

                    default:
                        Role empRole = roleRepo.findByName(ERole.EMPLOYEE).get();
                        roles.add(empRole);
                }
            });
        }
        System.out.println("these are the roles assigned");
        roles.forEach((x -> System.out.println(x.getName())));

        e.setRoles(roles);

        empRepo.save(e);
        return "signed up";

    }

    // @PostMapping("/login")
    // public ResponseEntity<?> authenticateUser(@RequestBody LoginReq loginreq) {

    // Authentication authentication = authenticationManager
    // .authenticate(new UsernamePasswordAuthenticationToken(loginreq.getUsername(),
    // loginreq.getPassword()));

    // SecurityContextHolder.getContext().setAuthentication(authentication);
    // // String jwt=jwtUtils.generateJwtToken(authentication);

    // }

    @RequestMapping("/login/auth")
    @ResponseBody
    public String auth(@RequestBody LoginReq loginReq) {
        Assert.notNull(loginReq.getUsername(), "the username must not be empty");
        Assert.notNull(loginReq.getPassword(), "the password must not be empty");

        System.out.println(loginReq.getUsername() + " " + loginReq.getPassword());

        return "auth successful";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Employee> getAllEmp() {
        return empRepo.findAll();

    }

}
