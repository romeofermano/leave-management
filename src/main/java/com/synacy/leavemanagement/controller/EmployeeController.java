package com.synacy.leavemanagement.controller;

import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.response.EmployeeListResponse;
import com.synacy.leavemanagement.response.EmployeeResponse;
import com.synacy.leavemanagement.response.PageResponse;
import com.synacy.leavemanagement.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("api/v1/employees")
    public PageResponse<EmployeeResponse> getEmployees(@RequestParam(value = "max", defaultValue = "5") int max,
                                                       @RequestParam(value = "page", defaultValue =  "1") int page) {
        Page<Employee> employees = employeeService.fetchEmployees(max, page);
        List<EmployeeResponse> employeeResponseList = employees.getContent().stream().map(EmployeeResponse::new)
                .collect(Collectors.toList());
        return new PageResponse<>(employeeService.fetchTotalEmployee(), page, employeeResponseList);
    }

    @GetMapping("api/v1/employees/list")
    public List<EmployeeListResponse> getListEmployees() {
        return null;
    }
}
