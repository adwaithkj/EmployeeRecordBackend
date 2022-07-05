package com.oracle.employeerecord.security.services;

import java.beans.JavaBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oracle.employeerecord.model.Employee;
import com.oracle.employeerecord.repo.EmpRepo;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    EmpRepo empRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Employee user = empRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found within the database" + username));

        return UserDetailsImpl.build(user);
    }

}
