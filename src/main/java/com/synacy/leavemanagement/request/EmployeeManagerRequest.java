package com.synacy.leavemanagement.request;

import com.synacy.leavemanagement.enums.RoleType;
import lombok.Getter;

@Getter
public class EmployeeManagerRequest {
    private String name;
    private RoleType roleType;
    private Integer totalLeaves;
}
