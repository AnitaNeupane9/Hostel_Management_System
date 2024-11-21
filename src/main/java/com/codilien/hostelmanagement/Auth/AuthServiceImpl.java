package com.codilien.hostelmanagement.Auth;

import com.codilien.hostelmanagement.Enum.Role;
import com.codilien.hostelmanagement.model.Employee;
import com.codilien.hostelmanagement.model.Student;
import com.codilien.hostelmanagement.repository.EmployeeRepository;
import com.codilien.hostelmanagement.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    private StudentRepository studentRepository ;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Override
    public boolean login(String email, String password, Role role, HttpSession session) {
        // Check for Student login
        if (role == Role.STUDENT) {
            Student student = studentRepository.findByEmail(email);
            if (student != null && student.getPassword().equals(password)) {
                session.setAttribute("userId", student.getId());
                session.setAttribute("role", Role.STUDENT.name());
                return true;  // Login successful for student
            }
        }

        // Check for Employee login
        if (role != Role.STUDENT) {
            Employee employee = employeeRepository.findByEmail(email);
            if (employee != null && employee.getPassword().equals(password)) {
                if (role == employee.getRole()) {
                    session.setAttribute("userId", employee.getId());
                    session.setAttribute("role", employee.getRole().name());
                    return true;
                }
            }
        }

        return false;  // Invalid credentials
    }

//    public boolean login(String email, String password, Role role, HttpSession session) {
//        // Check for Student login
//        if (role == Role.STUDENT) {
//            Student student = studentRepository.findByEmail(email);
//            if (student != null && student.getPassword().equals(password)) {
//                session.setAttribute("userId", student.getId());
//                session.setAttribute("role", Role.STUDENT.name());  // Stores the role as the string
////                session.setAttribute("role", Role.STUDENT);         // Stores the roles as Enum
//                return true;        // Login successful for student
//            }
//        }
//
//        // Check for Employee login
//        if (role != Role.STUDENT) {
//            Employee employee = employeeRepository.findByEmail(email);
//            if (employee != null && employee.getPassword().equals(password)) {
//                if (role == employee.getRole()) {
//                    session.setAttribute("userId", employee.getId());
//                    session.setAttribute("role", employee.getRole().name());
//                    return true;
//                }
//            }
//        }
//
//        return false;  // Invalid credentials
//    }

    @Override
    public void logout(HttpSession session) {
        session.invalidate();
    }
}
