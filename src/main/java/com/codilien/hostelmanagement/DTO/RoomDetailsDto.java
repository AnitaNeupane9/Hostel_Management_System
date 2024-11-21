package com.codilien.hostelmanagement.DTO;

import com.codilien.hostelmanagement.Enum.RoomStatus;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoomDetailsDto {
    private Long id;
    private String roomNumber;
    private int floor;
    private String roomType;

    @Min(1)
    private int capacity;
    @Min(0)
    private int currentStudentCount;
    @Min(0)
    private int availableStudentSlots;
    private RoomStatus status;
}
