package com.codilien.hostelmanagement.service;

import com.codilien.hostelmanagement.DTO.StudentDto;

import java.util.List;

public interface StudentService {

    StudentDto registerStudent (StudentDto studentDto);
    StudentDto getStudent (Long id);
    List<StudentDto> getAllStudent();
    StudentDto updateStudent(Long id, StudentDto studentDto);
    void deleteStudent(Long id);

}
