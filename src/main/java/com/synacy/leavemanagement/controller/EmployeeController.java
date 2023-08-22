package com.synacy.leavemanagement.controller;

import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.request.EmployeeManagerRequest;
import com.synacy.leavemanagement.response.EmployeeListResponse;
import com.synacy.leavemanagement.response.EmployeeManagerResponse;
import com.synacy.leavemanagement.response.PageResponse;
import com.synacy.leavemanagement.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/employees")
    public PageResponse<EmployeeManagerResponse> getEmployees(@RequestParam(value = "max", defaultValue = "5") int max,
                                                              @RequestParam(value = "page", defaultValue =  "1") int page) {
        Page<Employee> employees = employeeService.fetchEmployees(max, page);
        List<EmployeeManagerResponse> employeeManagerResponseList = employees.getContent().stream().map(EmployeeManagerResponse::new)
                .collect(Collectors.toList());
        return new PageResponse<>(employeeService.fetchTotalEmployee(), page, employeeManagerResponseList);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/employees/list")
    public List<EmployeeListResponse> getListEmployees() {
        List<Employee> employees = employeeService.fetchListEmployee();
        return employees.stream().map(EmployeeListResponse::new).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/employees/manager")
    public EmployeeManagerResponse createEmployee(@RequestParam(value = "adminId") Long adminId, @RequestBody EmployeeManagerRequest managerRequest) {
        Employee employee = employeeService.createEmployeeManager(adminId, managerRequest);
        return new EmployeeManagerResponse(employee);
    }
}
