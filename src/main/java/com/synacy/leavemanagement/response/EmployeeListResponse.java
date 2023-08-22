package com.synacy.leavemanagement.response;

import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.model.Employee;
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
