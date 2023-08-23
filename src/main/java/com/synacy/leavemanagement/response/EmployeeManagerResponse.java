package com.synacy.leavemanagement.response;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.model.Employee;
import lombok.Getter;

@Getter
public class EmployeeManagerResponse {
    private final Long id;
    private final String name;
    private final RoleType roleType;
    private final Integer totalLeaves;
    private final Integer currentLeaves;
    private final EmployeeStatus employeeStatus;

    public EmployeeManagerResponse(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.roleType = employee.getRoleType();
        this.totalLeaves = employee.getTotalLeaves();
        this.currentLeaves = employee.getCurrentLeaves();
        this.employeeStatus = employee.getEmployeeStatus();
    }
}