package com.synacy.leavemanagement.services;

import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.repository.EmployeeRepository;
import org.springframework.data.domain.Page;

public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Page<Employee> fetchEmployees(int max, int page) {

    }
}
