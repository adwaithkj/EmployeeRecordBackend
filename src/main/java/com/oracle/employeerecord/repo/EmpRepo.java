package com.oracle.employeerecord.repo;

import org.springframework.data.repository.CrudRepository;

import com.oracle.employeerecord.model.Employee;

public interface EmpRepo extends CrudRepository<Employee, Long> {

}
