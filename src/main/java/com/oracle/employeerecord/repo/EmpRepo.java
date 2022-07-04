package com.oracle.employeerecord.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.oracle.employeerecord.model.Employee;

public interface EmpRepo extends CrudRepository<Employee, Long> {
    Optional<Employee> findByUsername(String username);

    Boolean existsByUsername(String username);
}
