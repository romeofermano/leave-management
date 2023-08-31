package com.synacy.leavemanagement.leave;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequest {
    @NotNull(message = "Employee is required.")
    Long employee_id;

    @NotNull(message = "Leave start date is required.")
    LocalDate startDate;

    @NotNull(message = "Leave end date is required.")
    LocalDate endDate;

    @NotNull(message = "Leave reason is required.")
    @NotBlank(message = "Leave reason is required.")
    String reason;
}
