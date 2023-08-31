package com.synacy.leavemanagement.employee.request;

import com.synacy.leavemanagement.enums.RoleType;
import lombok.Getter;

@Getter
public class EmployeeManagerRequest {
    private String name;
    private RoleType roleType;
    private Integer totalLeaves;
}
