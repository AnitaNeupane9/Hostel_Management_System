package com.codilien.hostelmanagement.service;

import com.codilien.hostelmanagement.DTO.PaymentDto;

import java.util.List;

public interface PaymentService {
    PaymentDto createPayment(PaymentDto paymentDto);
    PaymentDto getPayemnt(Long id);
    List<PaymentDto> getAllPayment ();
    PaymentDto updatePayment(Long id, PaymentDto paymentDto);
    void deletePayment(Long id);

}
