package com.codilien.hostelmanagement.exception;

public class StudentNotFoundException extends RuntimeException {

    // Constructor that accepts the student ID and automatically creates the message
    public StudentNotFoundException(Long studentId) {
        super("Student with ID " + studentId + " not found. Please ensure the student exists.");
    }
}
