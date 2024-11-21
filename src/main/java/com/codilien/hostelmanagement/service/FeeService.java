package com.codilien.hostelmanagement.service;

import com.codilien.hostelmanagement.DTO.FeeDto;

import java.util.List;

public interface FeeService {
    FeeDto createFee(FeeDto feeDto);
    FeeDto getFee(Long id);
    List<FeeDto> getAllFee();
    FeeDto updateFee(Long id, FeeDto feeDto);
    void deleteFee(Long id);
    void applyPenaltyIfDueDatePassed();

}
