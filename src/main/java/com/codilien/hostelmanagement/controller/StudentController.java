package com.codilien.hostelmanagement.controller;

import com.codilien.hostelmanagement.exception.StudentNotFoundException;
import com.codilien.hostelmanagement.DTO.StudentDto;
import com.codilien.hostelmanagement.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping("/register")
    public ResponseEntity<StudentDto> registerStudent(@RequestBody StudentDto studentDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.registerStudent(studentDto));
    }

//    @PostMapping("/register")
//    public ResponseEntity<StudentDto> registerStudent(
//            @RequestPart("profilePicture") MultipartFile profilePicture,
//            @RequestPart StudentDto studentDto) {
//
//        // Set the profile picture in the DTO
//        studentDto.setProfilePicture(profilePicture);
//
//        return ResponseEntity.status(HttpStatus.CREATED).body(studentService.registerStudent(studentDto));
//    }


    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudent(@PathVariable Long id){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(studentService.getStudent(id));
        } catch (Exception e) {
            throw new StudentNotFoundException(id);
        }
    }

    @GetMapping({"/", " "})
    public ResponseEntity<List<StudentDto>> getAllStudent(){
        List<StudentDto> students = studentService.getAllStudent();
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<StudentDto> updateStudent (@PathVariable Long id, @RequestBody StudentDto studentDto){
        return ResponseEntity.status(HttpStatus.OK).body(studentService.updateStudent(id, studentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully.");
    }
}
