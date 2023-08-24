package com.synacy.leavemanagement.leave;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class LeaveRequest {
    Long employee_id;
    LocalDate startDate;
    LocalDate endDate;
    Integer days;
    String reason;
}
