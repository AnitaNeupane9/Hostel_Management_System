package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.exception.StudentNotFoundException;
import com.codilien.hostelmanagement.DTO.StudentDto;
import com.codilien.hostelmanagement.Enum.Role;
import com.codilien.hostelmanagement.mapper.StudentMapper;
import com.codilien.hostelmanagement.model.Student;
import com.codilien.hostelmanagement.repository.EmployeeRepository;
import com.codilien.hostelmanagement.repository.StudentRepository;
import com.codilien.hostelmanagement.service.StudentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private StudentRepository studentRepository;
    private EmployeeRepository employeeRepository;

    public StudentServiceImpl(StudentRepository studentRepository, EmployeeRepository employeeRepository) {
        this.studentRepository = studentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public StudentDto registerStudent(StudentDto studentDto) {

        Student student = StudentMapper.mapToStudent(studentDto);

        if (studentRepository.existsByEmail(studentDto.getEmail()) || employeeRepository.existsByEmail(studentDto.getEmail())){
            throw new IllegalArgumentException("Email is already in use.");
        }

        // Setting the Role and isActive as STUDENT and true
        student.setRole(Role.STUDENT);
        student.setActive(true);

        Student registeredStudent = studentRepository.save(student);
        return StudentMapper.mapToStudentDto(registeredStudent);

    }

//    @Override
//    public StudentDto registerStudent(StudentDto studentDto) {
//
//        Student student = StudentMapper.mapToStudent(studentDto);
//
//        // Check if email is already in use
//        if (studentRepository.existsByEmail(studentDto.getEmail()) || employeeRepository.existsByEmail(studentDto.getEmail())) {
//            throw new IllegalArgumentException("Email is already in use.");
//        }
//
//        // file upload and get the file path
//        String profilePicturePath = storeProfilePicture(studentDto.getProfilePicture());
//        student.setProfilePicture(profilePicturePath);
//
//        student.setRole(Role.STUDENT);
//
//        Student registeredStudent = studentRepository.save(student);
//        return StudentMapper.mapToStudentDto(registeredStudent);
//    }

    @Override
    public StudentDto getStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        return StudentMapper.mapToStudentDto(student);
    }

    @Override
    public List<StudentDto> getAllStudent() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> StudentMapper.mapToStudentDto(student))
                .collect(Collectors.toList());
    }

    @Override
    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student toBeUpdated = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        toBeUpdated.setName(studentDto.getName());
        toBeUpdated.setGender(studentDto.getGender());
        toBeUpdated.setAddress(studentDto.getAddress());
        toBeUpdated.setNationality(studentDto.getNationality());
        toBeUpdated.setDateOfBirth(studentDto.getDateOfBirth());
        toBeUpdated.setContactNumber(studentDto.getContactNumber());
        toBeUpdated.setEmail(studentDto.getEmail());
        toBeUpdated.setParentContact(studentDto.getParentContact());
        toBeUpdated.setEmergencyContact(studentDto.getEmergencyContact());
        toBeUpdated.setIdProof(studentDto.getIdProof());
        toBeUpdated.setPassword(studentDto.getPassword());
        toBeUpdated.setActive(studentDto.isActive());

        Student updatedStudent = studentRepository.save(toBeUpdated);
        return StudentMapper.mapToStudentDto(updatedStudent);
    }

    @Override
    public void deleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
        studentRepository.delete(student);
    }


//    // method for the storing profile picture
//    private String storeProfilePicture(MultipartFile profilePicture) {
//
//        String uniqueId = UUID.randomUUID().toString();
//        String storageFileName = uniqueId + "_" + profilePicture.getOriginalFilename();
//
//        try {
//            //directory where profile pictures will be stored
//            String uploadDir = "public/images/profilePicture/";
//            Path uploadPath = Paths.get(uploadDir);
//
//            // Create the directory if doesn’t exist
//            if (!Files.exists(uploadPath)) {
//                Files.createDirectories(uploadPath);
//            }
//
//            // Save the file
//            try (InputStream inputStream = profilePicture.getInputStream()) {
//                Files.copy(inputStream, uploadPath.resolve(storageFileName), StandardCopyOption.REPLACE_EXISTING);
//            }
//
//            // Return the complete path to be saved in the database
//            return uploadDir + storageFileName;
//        } catch (Exception ex) {
//            throw new RuntimeException("Failed to store profile picture: " + ex.getMessage());
//        }
//    }
}
