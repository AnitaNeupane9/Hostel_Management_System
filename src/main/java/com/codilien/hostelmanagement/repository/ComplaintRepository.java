package com.codilien.hostelmanagement.repository;

import com.codilien.hostelmanagement.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComplaintRepository  extends JpaRepository<Complaint, Long> {
}
