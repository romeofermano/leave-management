package com.synacy.leavemanagement.employee.model.services;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.employee.model.Employee;
import com.synacy.leavemanagement.employee.model.repository.EmployeeRepository;
import com.synacy.leavemanagement.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> fetchEmployees(int max, int page) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                Arrays.asList(RoleType.MANAGER, RoleType.MEMBER), pageable);
    }

    // TODO: List all employees with the employee status is active
    // TODO: Create new employees
    // TODO: Update existing employees
    // TODO: Terminate employees
}
