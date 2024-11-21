package com.codilien.hostelmanagement.repository;

import com.codilien.hostelmanagement.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByEmail(String email);
    Student findByEmail(String email);
}
