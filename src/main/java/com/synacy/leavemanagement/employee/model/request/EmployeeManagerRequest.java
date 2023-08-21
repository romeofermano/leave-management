package com.synacy.leavemanagement.employee.model.request;

import com.synacy.leavemanagement.enums.RoleType;
import lombok.Getter;

@Getter
public class EmployeeManagerRequest {
    String name;
    RoleType roleType;
    Integer totalLeaves;
}
