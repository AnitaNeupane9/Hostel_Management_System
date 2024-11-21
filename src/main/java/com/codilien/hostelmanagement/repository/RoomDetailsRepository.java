package com.codilien.hostelmanagement.repository;

import com.codilien.hostelmanagement.model.RoomDetails;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoomDetailsRepository extends JpaRepository<RoomDetails, Long> {
    boolean existsByRoomNumber(String RoomNumber);
}
