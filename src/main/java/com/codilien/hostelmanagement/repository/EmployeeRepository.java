package com.codilien.hostelmanagement.repository;

import com.codilien.hostelmanagement.Enum.Role;
import com.codilien.hostelmanagement.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    boolean existsByEmail(String email);
    Employee findByEmail(String email);
    Optional<Employee> findByRole(Role role);
}
