package com.codilien.hostelmanagement.model;

import com.codilien.hostelmanagement.BaseEntity;
import com.codilien.hostelmanagement.Enum.RoomStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class RoomDetails extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Enumerated(EnumType.STRING)
    private RoomStatus status;

    @OneToMany(mappedBy = "roomDetails")
    private List<RoomAllotment> roomAllotments; // One room can have multiple allotments

    public RoomDetails(Long id, String roomNumber, int floor, String roomType, int capacity, RoomStatus status) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.floor = floor;
        this.roomType = roomType;
        this.capacity = capacity;
        this.status = status;
    }


}
