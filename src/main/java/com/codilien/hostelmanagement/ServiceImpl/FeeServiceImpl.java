package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.exception.StudentNotFoundException;
import com.codilien.hostelmanagement.DTO.FeeDto;
import com.codilien.hostelmanagement.model.Fee;
import com.codilien.hostelmanagement.model.Student;
import com.codilien.hostelmanagement.repository.FeeRepository;
import com.codilien.hostelmanagement.repository.StudentRepository;
import com.codilien.hostelmanagement.service.FeeService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FeeServiceImpl implements FeeService {

    private FeeRepository feeRepository;
    private StudentRepository studentRepository;

    @Autowired
    public FeeServiceImpl(FeeRepository feeRepository, StudentRepository studentRepository) {
        this.feeRepository = feeRepository;
        this.studentRepository = studentRepository;
    }

    @Override
    public FeeDto createFee(FeeDto feeDto) {
        Optional<Student> studentOpt = studentRepository.findById(feeDto.getStudentId());
        if (studentOpt.isEmpty()) {
            throw new StudentNotFoundException(feeDto.getStudentId());
        }

        Student student = studentOpt.get();
        // Check if the Student status is Active
        if (!student.isActive()) {
            throw new RuntimeException("Fee cannot be created. Student status is inactive.");
        }

        Fee fee = new Fee();
        fee.setActualAmount(feeDto.getActualAmount());
        fee.setPenaltyFee(0.0);
        fee.setNetAmount(fee.getActualAmount());
        fee.setRemainingAmount(fee.getActualAmount());
        fee.setDueDate(feeDto.getDueDate());
        fee.setFeeType(feeDto.getFeeType());
        fee.setStudent(studentOpt.get());

        Fee savedFee = feeRepository.save(fee);
        return mapToDto(savedFee);
    }

    @Override
    public FeeDto getFee(Long id) {
        Fee fee = feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee Record doesn't exist."));

        return mapToDto(fee);
    }

    @Override
    public List<FeeDto> getAllFee() {
        List<Fee> fees = feeRepository.findAll();
        return fees.stream()
                .map(fee -> mapToDto(fee))
                .collect(Collectors.toList());
    }

//    @Override
//    @Transactional
//    public FeeDto updateFee(Long id, FeeDto feeDto) {
//        Optional<Fee> existingFeeOpt = feeRepository.findById(id);
//        if (existingFeeOpt.isEmpty()){
//            throw new RuntimeException("Fee record doesn't exist.");
//        }
//
//        Fee toBeUpdated  = existingFeeOpt.get();
//        toBeUpdated.setActualAmount(feeDto.getActualAmount());
//        toBeUpdated.setDueDate(feeDto.getDueDate());
//
//        if (toBeUpdated.getPenaltyFee() != null) {
//            double penalty = toBeUpdated.getPenaltyFee().doubleValue();
//        } else {
//            double penalty = 0.0;
//        }
//
//        // Calculate net amount if it's not provided (actual amount + penalty fee)
//        double netAmount;
//        if (feeDto.getNetAmount() != null) {
//            netAmount = feeDto.getNetAmount();
//        } else {
//            netAmount = toBeUpdated.getActualAmount() + toBeUpdated.getPenaltyFee();
//        }
//        toBeUpdated.setNetAmount(netAmount);
//
//        // Update remaining amount if it's provided, otherwise set same as netAmount
//        if (feeDto.getRemainingAmount() != null) {
//            toBeUpdated.setRemainingAmount(feeDto.getRemainingAmount());
//        } else {
//            toBeUpdated.setRemainingAmount(netAmount);
//        }
//
//
//        toBeUpdated.setFeeType(feeDto.getFeeType());
//
//        // Update the studentId (as they might add fee to wrong student)
//        if (feeDto.getStudentId() != null) {
//            Optional<Student> studentOpt = studentRepository.findById(feeDto.getStudentId());
//            if (studentOpt.isEmpty()) {
//                throw new StudentNotFoundException(feeDto.getStudentId());
//            }
//
//            Student student = studentOpt.get();
//            // Check if the Student status is Active
//            if (!student.isActive()) {
//                throw new RuntimeException("Fee cannot be updated. Student status is inactive.");
//            }
//
//            toBeUpdated.setStudent(student);
//        }
//
//        Fee updatedFee = feeRepository.save(toBeUpdated);
//        return mapToDto(updatedFee);
//    }

    @Override
    @Transactional
    public FeeDto updateFee(Long id, FeeDto feeDto) {
        Optional<Fee> existingFeeOpt = feeRepository.findById(id);
        if (existingFeeOpt.isEmpty()) {
            throw new RuntimeException("Fee record doesn't exist.");
        }

        Fee toBeUpdated = existingFeeOpt.get();
        toBeUpdated.setActualAmount(feeDto.getActualAmount());
        toBeUpdated.setDueDate(feeDto.getDueDate());

        // Set penalty fee to 0 if it's null
        double penaltyFee = 0.0;
        if (toBeUpdated.getPenaltyFee() != null) {
            penaltyFee = toBeUpdated.getPenaltyFee();
        }

        // Calculate net amount (use provided value or calculate with actual + penalty)
        double netAmount = 0.0;
        if (feeDto.getNetAmount() != null) {
            netAmount = feeDto.getNetAmount();
        } else {
            netAmount = toBeUpdated.getActualAmount() + penaltyFee;
        }
        toBeUpdated.setNetAmount(netAmount);

        // Set remaining amount
        double remainingAmount = netAmount;
        if (feeDto.getRemainingAmount() != null) {
            remainingAmount = feeDto.getRemainingAmount();
        }
        toBeUpdated.setRemainingAmount(remainingAmount);

        toBeUpdated.setFeeType(feeDto.getFeeType());

        // Update student if studentId is provided
        if (feeDto.getStudentId() != null) {
            Optional<Student> studentOpt = studentRepository.findById(feeDto.getStudentId());
            if (studentOpt.isEmpty()) {
                throw new StudentNotFoundException(feeDto.getStudentId());
            }

            Student student = studentOpt.get();
            if (!student.isActive()) {
                throw new RuntimeException("Fee cannot be updated. Student status is inactive.");
            }

            toBeUpdated.setStudent(student);
        }

        // Save the updated fee and return DTO
        Fee updatedFee = feeRepository.save(toBeUpdated);
        return mapToDto(updatedFee);
    }


    @Override
    public void deleteFee(Long id) {
        Fee toBeDeleted = feeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Fee record doesn't exist."));
        feeRepository.delete(toBeDeleted);
    }


    public void applyPenaltyIfDueDatePassed() {
        LocalDate currentDate = LocalDate.now();

        // Find all fees that have crossed the due date and are unpaid
        List<Fee> overdueFees = feeRepository.findOverdue(currentDate, 0);

        for (Fee fee : overdueFees) {
            double penaltyFee = fee.getActualAmount() * 0.10;
            fee.setPenaltyFee(penaltyFee);
            fee.setNetAmount(fee.getActualAmount() + penaltyFee);
            fee.setRemainingAmount(fee.getNetAmount());
            feeRepository.save(fee);
        }
    }


    //Helper Method to convert entity to Dto
    private FeeDto mapToDto(Fee fee){
        FeeDto feeDto = new FeeDto();
        feeDto.setId(fee.getId());
        feeDto.setActualAmount(fee.getActualAmount());
        feeDto.setDueDate(fee.getDueDate());
        feeDto.setPenaltyFee(fee.getPenaltyFee());
        feeDto.setNetAmount(fee.getNetAmount());
        feeDto.setRemainingAmount(fee.getRemainingAmount());
        feeDto.setFeeType(fee.getFeeType());
        feeDto.setStudentId(fee.getStudent().getId());
        return feeDto;
    }
}
