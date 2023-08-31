package com.synacy.leavemanagement.employee.request;

import com.synacy.leavemanagement.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class EmployeeRequest {
    @NotNull(message = "Employee name is required.")
    @NotBlank(message = "Employee name is required.")
    private String name;

    @NotNull(message = "Employee role is required.")
    private RoleType roleType;

    @Positive(message = "Employee leave must be greater than 1.")
    private Integer totalLeaves;

    @NotNull(message = "Employee manager is required.")
    private Long managerId;
}
