package com.synacy.leavemanagement.employee.request;

import com.synacy.leavemanagement.enums.RoleType;
import lombok.Getter;

@Getter
public class EmployeeMemberRequest {
    private String name;
    private RoleType roleType;
    private Integer totalLeaves;
    private Long managerId;
}
