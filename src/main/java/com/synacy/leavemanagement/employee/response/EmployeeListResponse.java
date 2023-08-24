package com.synacy.leavemanagement.employee.response;

import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.enums.RoleType;
import lombok.Getter;

@Getter
public class EmployeeListResponse {
    private final String name;
    private final RoleType roleType;

    public EmployeeListResponse(Employee employee) {
        this.name = employee.getName();
        this.roleType = employee.getRoleType();
    }
}
