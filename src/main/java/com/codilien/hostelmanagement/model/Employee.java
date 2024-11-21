package com.codilien.hostelmanagement.model;

import com.codilien.hostelmanagement.BaseEntity;
import com.codilien.hostelmanagement.Enum.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Employee extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactNumber;
    private String address;
    private String email;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;
    private LocalDate hireDate;
    private LocalDate employmentEndDate;

    private LocalTime shiftStartsAt;
    private LocalTime shiftEndsAt;
    private String idProof;

}
