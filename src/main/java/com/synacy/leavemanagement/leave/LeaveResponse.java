package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LeaveResponse {
    private final Long id;
    private final String employeeName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer days;
    private final String reason;
    private final LeaveStatus leaveStatus;


    public LeaveResponse(Long id, String employeeName, LocalDate startDate, LocalDate endDate, Integer days, String reason, LeaveStatus leaveStatus) {
        this.id = id;
        this.employeeName = employeeName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.days = days;
        this.reason = reason;
        this.leaveStatus = leaveStatus;
    }
}
