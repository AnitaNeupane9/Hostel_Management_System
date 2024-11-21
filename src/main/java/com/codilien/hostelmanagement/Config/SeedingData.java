package com.codilien.hostelmanagement.Config;

import com.codilien.hostelmanagement.Enum.Role;
import com.codilien.hostelmanagement.model.Employee;
import com.codilien.hostelmanagement.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SeedingData implements CommandLineRunner {

    private EmployeeRepository employeeRepository;

    @Autowired
    public SeedingData(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        Optional<Employee> existingAdmin = employeeRepository.findByRole(Role.valueOf("ADMIN"));
        // If no admin is found, insert the hardcoded admin data
        if (existingAdmin.isEmpty()) {
            Employee admin = new Employee();
            admin.setName("Admin");
            admin.setContactNumber("+977 9845007443");
            admin.setAddress("Rasuwa");
            admin.setEmail("admin@gmail.com");
            admin.setRole(Role.ADMIN);
            admin.setPassword("@admin9");

            employeeRepository.save(admin);
        }
    }
}
