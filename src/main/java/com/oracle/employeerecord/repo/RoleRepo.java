package com.oracle.employeerecord.repo;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.oracle.employeerecord.model.ERole;
import com.oracle.employeerecord.model.Role;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

}
