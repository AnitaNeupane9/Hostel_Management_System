package com.codilien.hostelmanagement.DTO;

import com.codilien.hostelmanagement.Enum.FeeType;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeeDto {
    private Long id;

    @Min(0)
    private Double actualAmount;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dueDate;
    @Min(0)
    private Double penaltyFee;
    @Min(0)
    private Double netAmount;
    private Double remainingAmount;
    private FeeType feeType;
    private Long studentId;

}
