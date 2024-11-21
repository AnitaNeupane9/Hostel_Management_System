package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.DTO.EmployeeDto;
import com.codilien.hostelmanagement.mapper.EmployeeMapper;
import com.codilien.hostelmanagement.model.Employee;
import com.codilien.hostelmanagement.repository.EmployeeRepository;
import com.codilien.hostelmanagement.repository.StudentRepository;
import com.codilien.hostelmanagement.service.EmployeeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private StudentRepository studentRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, StudentRepository studentRepository) {
        this.employeeRepository = employeeRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        Employee employee = EmployeeMapper.mapToEmployee(employeeDto);

        if (employeeRepository.existsByEmail(employeeDto.getEmail()) || studentRepository.existsByEmail(employeeDto.getEmail()))
        {
            throw new RuntimeException("Email is already in use.");
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.mapToEmployeeDto(savedEmployee);
    }

    @Override
    public EmployeeDto getEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee doesn't exist."));

        return EmployeeMapper.mapToEmployeeDto(employee);
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto employeeDto) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Employee doesn't exist."));
        existingEmployee.setName(employeeDto.getName());
        existingEmployee.setContactNumber(employeeDto.getContactNumber());
        existingEmployee.setAddress(employeeDto.getAddress());
        existingEmployee.setRole(employeeDto.getRole());
        existingEmployee.setHireDate(employeeDto.getHireDate());
        existingEmployee.setEmploymentEndDate(employeeDto.getEmploymentEndDate());
        existingEmployee.setShiftStartsAt(employeeDto.getShiftStartsAt());
        existingEmployee.setShiftEndsAt(employeeDto.getShiftEndsAt());
        existingEmployee.setIdProof(employeeDto.getIdProof());

        Employee updatedEmployee = employeeRepository.save(existingEmployee);

        return EmployeeMapper.mapToEmployeeDto(updatedEmployee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(employee -> EmployeeMapper.mapToEmployeeDto((employee)))
                .collect(Collectors.toList());
    }


    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee doesn't exist."));
        employeeRepository.delete(employee);
    }
}
