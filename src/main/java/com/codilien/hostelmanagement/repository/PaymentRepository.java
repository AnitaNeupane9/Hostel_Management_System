package com.codilien.hostelmanagement.repository;

import com.codilien.hostelmanagement.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
