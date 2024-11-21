package com.codilien.hostelmanagement.repository;

import com.codilien.hostelmanagement.model.RoomAllotment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomAllotmentRepository extends JpaRepository<RoomAllotment, Long> {
}
