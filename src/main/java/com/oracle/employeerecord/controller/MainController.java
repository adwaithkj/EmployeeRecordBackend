package com.oracle.employeerecord.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
import com.oracle.employeerecord.payload.response.JwtResponse;
import com.oracle.employeerecord.payload.response.MessageResponse;
import com.oracle.employeerecord.repo.EmpRepo;
import com.oracle.employeerecord.repo.RoleRepo;
import com.oracle.employeerecord.security.jwt.JwtUtils;
import com.oracle.employeerecord.security.services.UserDetailsImpl;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
// import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping("/employees")
public class MainController {
    @Autowired
    private EmpRepo empRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping(path = "/signup")
    public ResponseEntity<?> signup(@RequestBody SignupReq signupReq) {

        if (empRepo.existsByUsername(signupReq.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("the username is already taken"));
        }
        Employee e = new Employee();
        e.setName(signupReq.getName());
        e.setPassword(encoder.encode(signupReq.getPassword()));
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
        return ResponseEntity.ok(new MessageResponse("user reg successful"));

    }

    @RequestMapping("/login/auth")
    @ResponseBody
    public ResponseEntity<?> auth(@RequestBody LoginReq loginReq) {
        Assert.notNull(loginReq.getUsername(), "the username must not be empty");
        Assert.notNull(loginReq.getPassword(), "the password must not be empty");

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName(),
                roles));
    }

    @GetMapping(path = "/loginpage")
    public String loginpage(Model model) {
        model.addAttribute("loginReq", new LoginReq());

        return "index";
    }

    @PostMapping(path = "/loginpage")
    public String htmlform(@ModelAttribute LoginReq loginReq, Model model) {
        // model.addAttribute("loginReq", loginReq);

        ResponseEntity re = auth(loginReq);
        // System.out.println(re.getClass().);
        model.addAttribute("response", re);
        return "response";
    }

    @GetMapping(path = "/all")
    public @ResponseBody Iterable<Employee> getAllEmp() {

        return empRepo.findAll();

    }

}
