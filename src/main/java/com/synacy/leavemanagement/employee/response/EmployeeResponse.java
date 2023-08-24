package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.employee.Employee;
import lombok.Getter;

@Getter
public class EmployeeResponse {
    private final Long id;
    private final String name;
    private final RoleType roleType;
    private final Integer totalLeaves;
    private final Integer currentLeaves;
    private final EmployeeStatus employeeStatus;
    private final String manager;

    public EmployeeResponse(Employee employee) {
        this.id = employee.getId();
        this.name = employee.getName();
        this.roleType = employee.getRoleType();
        this.totalLeaves = employee.getTotalLeaves();
        this.currentLeaves = employee.getCurrentLeaves();
        this.employeeStatus = employee.getEmployeeStatus();
        this.manager = employee.getManager().getName();
    }
}
