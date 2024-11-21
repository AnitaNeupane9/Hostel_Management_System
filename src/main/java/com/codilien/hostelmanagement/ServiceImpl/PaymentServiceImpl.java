package com.codilien.hostelmanagement.ServiceImpl;

import com.codilien.hostelmanagement.DTO.PaymentDto;
import com.codilien.hostelmanagement.model.Fee;
import com.codilien.hostelmanagement.model.Payment;
import com.codilien.hostelmanagement.repository.FeeRepository;
import com.codilien.hostelmanagement.repository.PaymentRepository;
import com.codilien.hostelmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final FeeRepository feeRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, FeeRepository feeRepository) {
        this.paymentRepository = paymentRepository;
        this.feeRepository = feeRepository;
    }


    @Override
    @Transactional
    public PaymentDto createPayment(PaymentDto paymentDto) {
        Payment payment = new Payment();
        payment.setPaymentAmount(paymentDto.getPaymentAmount());
        payment.setPaymentDate(paymentDto.getPaymentDate());
        payment.setPaymentMethod(paymentDto.getPaymentMethod());
        payment.setRemarks(paymentDto.getRemarks());

        Optional<Fee> fee = feeRepository.findById(paymentDto.getFeeId());
        if (fee.isPresent()) {
            payment.setFee(fee.get());
        }else{
            throw new RuntimeException("Fee record doesn't exist.");
        }

        Fee actualFee = fee.get();

        if (paymentDto.getPaymentAmount() > actualFee.getActualAmount()){
            throw new RuntimeException("Payment amount can't be greater than the remaining amount.");
        }

        //update remainingFee on the Fee table once the payment amount is entered.
        actualFee.setRemainingAmount(actualFee.getRemainingAmount() - paymentDto.getPaymentAmount());

        Payment savedPayment = paymentRepository.save(payment);
        return mapToDto(savedPayment);
    }

    @Override
    public PaymentDto getPayemnt(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        if (payment.isPresent()) {
            return mapToDto(payment.get());
        }
        else{
            throw new RuntimeException("Payment record doesn't exist.");
        }
    }

    @Override
    public List<PaymentDto> getAllPayment() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(payment -> mapToDto(payment))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDto updatePayment(Long id, PaymentDto paymentDto) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);

        if (paymentOpt.isPresent()) {
            Payment payment = paymentOpt.get();
            payment.setPaymentAmount(paymentDto.getPaymentAmount());
            payment.setPaymentDate(paymentDto.getPaymentDate());
            payment.setPaymentMethod(paymentDto.getPaymentMethod());
            payment.setRemarks(paymentDto.getRemarks());

            Fee fee = feeRepository.findById(paymentDto.getFeeId())
                    .orElseThrow(() -> new RuntimeException("Fee record doesn't exist."));
            payment.setFee(fee);

            if (paymentDto.getPaymentAmount() > fee.getActualAmount()){
                throw new RuntimeException("Payment amount can't be greater than the remaining amount.");
            }

            //update the Remaining amount on the fee table once the paymentAmount is updated.
            fee.setRemainingAmount(fee.getRemainingAmount() - paymentDto.getPaymentAmount());

            Payment updatedPayment = paymentRepository.save(payment);
            return mapToDto(updatedPayment);
        }
        else{
            throw new RuntimeException("Payment record doesn't exist.");
        }
    }

    @Override
    public void deletePayment(Long id) {
        Payment toBeDeleted = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment record doesn't exist."));
        paymentRepository.delete(toBeDeleted);
    }

    private PaymentDto mapToDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setId(payment.getId());
        paymentDto.setPaymentAmount(payment.getPaymentAmount());
        paymentDto.setPaymentDate(payment.getPaymentDate());
        paymentDto.setPaymentMethod(payment.getPaymentMethod());
        paymentDto.setRemarks(payment.getRemarks());

        if (payment.getFee() != null) {
            paymentDto.setFeeId(payment.getFee().getId());
        }

        return paymentDto;
    }
}
