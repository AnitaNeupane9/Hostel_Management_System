package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.exception.StudentNotFoundException;
import com.codilien.hostelmanagement.DTO.ComplaintDto;
import com.codilien.hostelmanagement.model.Complaint;
import com.codilien.hostelmanagement.model.Student;
import com.codilien.hostelmanagement.repository.ComplaintRepository;
import com.codilien.hostelmanagement.repository.StudentRepository;
import com.codilien.hostelmanagement.service.ComplaintService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    private final ComplaintRepository complaintRepository;
    private final StudentRepository studentRepository;

    @Autowired
    public ComplaintServiceImpl(ComplaintRepository complaintRepository, StudentRepository studentRepository) {
        this.complaintRepository = complaintRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public ComplaintDto createComplaint(ComplaintDto complaintDto) {

        Optional<Student> studentOpt = studentRepository.findById(complaintDto.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new StudentNotFoundException(complaintDto.getStudentId());
        }

        Complaint complaint = new Complaint();
        complaint.setId(complaintDto.getId());
        complaint.setDateFiled(complaintDto.getDateFiled());
        complaint.setTopic(complaintDto.getTopic());
        complaint.setDescription(complaintDto.getDescription());
        complaint.setDateResolved(complaintDto.getDateResolved());
        complaint.setSeverityLevel(complaintDto.getSeverityLevel());
        complaint.setResolved(false);
        complaint.setStudent(studentOpt.get());

        Complaint savedComplaint = complaintRepository.save(complaint);
        return mapToDTO(savedComplaint);
    }

    @Override
    public ComplaintDto getComplaint(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with ID: " + id));
        return mapToDTO(complaint);
    }

    @Override
    public List<ComplaintDto> getAllComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();
        return complaints.stream()
                .map(complaint -> mapToDTO(complaint))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComplaints(Long id) {
        Complaint complaint = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint doesn't exist"));
        complaintRepository.delete(complaint);
    }

    @Override
    public ComplaintDto updateComplaint(Long id, ComplaintDto complaintDto, HttpSession session) {
        Complaint toBeUpdated = complaintRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint doesn't exist."));
        toBeUpdated.setDateFiled(complaintDto.getDateFiled());
        toBeUpdated.setTopic(complaintDto.getTopic());
        toBeUpdated.setDescription(complaintDto.getDescription());
        toBeUpdated.setSeverityLevel(complaintDto.getSeverityLevel());

        // only "ADMIN" or  "WARDEN can update these fields
        String role = (String) session.getAttribute("role");
        if ("ADMIN".equals(role) || "WARDEN".equals(role)) {

            toBeUpdated.setDateResolved(complaintDto.getDateResolved());
            toBeUpdated.setResolved(complaintDto.isResolved());
        }

        Complaint updatedComplaint = complaintRepository.save(toBeUpdated);
        return mapToDTO(updatedComplaint);
    }

    // Helper method to map Complaint entity to ComplaintDTO
    private ComplaintDto mapToDTO(Complaint complaint) {
        ComplaintDto dto = new ComplaintDto();
        dto.setId(complaint.getId());
        dto.setDateFiled(complaint.getDateFiled());
        dto.setTopic(complaint.getTopic());
        dto.setDescription(complaint.getDescription());
        dto.setSeverityLevel(complaint.getSeverityLevel());
        dto.setResolved(complaint.isResolved());
        dto.setDateResolved(complaint.getDateResolved());
        dto.setStudentId(complaint.getStudent().getId());
        return dto;
    }
}
