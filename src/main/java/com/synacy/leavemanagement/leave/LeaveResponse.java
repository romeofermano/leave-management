package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class LeaveResponse {
    private final Long id;
    private final String employeeName;
    private final String managerName;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final Integer days;
    private final String reason;
    private final LeaveStatus leaveStatus;


    public LeaveResponse(Leave leave) {
        this.id = leave.getId();
        this.employeeName = leave.getEmployee().getName();
        this.managerName = leave.getEmployee().getManager().getName();
        this.startDate = leave.getStartDate();
        this.endDate = leave.getEndDate();
        this.days = leave.getDays();
        this.reason = leave.getReason();
        this.leaveStatus = leave.getLeaveStatus();
    }

}
