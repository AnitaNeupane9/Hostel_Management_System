package com.codilien.hostelmanagement.mapper;

import com.codilien.hostelmanagement.DTO.EmployeeDto;
import com.codilien.hostelmanagement.model.Employee;

public class EmployeeMapper {

    // Map from EmployeeDto to Employee
    public static Employee mapToEmployee(EmployeeDto employeeDto) {
        Employee employee = new Employee(
                employeeDto.getId(),
                employeeDto.getName(),
                employeeDto.getContactNumber(),
                employeeDto.getAddress(),
                employeeDto.getEmail(),
                employeeDto.getRole(),
                employeeDto.getPassword(),
                employeeDto.getHireDate(),
                employeeDto.getEmploymentEndDate(),
                employeeDto.getShiftStartsAt(),
                employeeDto.getShiftEndsAt(),
                employeeDto.getIdProof()
        );
        return employee;
    }

    // Map from Employee to EmployeeDto
    public static EmployeeDto mapToEmployeeDto(Employee employee) {
        return new EmployeeDto(
                employee.getId(),
                employee.getName(),
                employee.getContactNumber(),
                employee.getAddress(),
                employee.getEmail(),
                employee.getRole(),
                employee.getPassword(),
                employee.getHireDate(),
                employee.getEmploymentEndDate(),
                employee.getShiftStartsAt(),
                employee.getShiftEndsAt(),
                employee.getIdProof()
        );
    }
}
