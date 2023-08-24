package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.employee.request.EmployeeManagerRequest;
import com.synacy.leavemanagement.employee.request.EmployeeMemberRequest;
import com.synacy.leavemanagement.employee.response.EmployeeListResponse;
import com.synacy.leavemanagement.employee.response.EmployeeResponse;
import com.synacy.leavemanagement.web.exceptions.response.PageResponse;
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
    public PageResponse<EmployeeResponse> getEmployees(@RequestParam(value = "max", defaultValue = "5") int max,
                                                       @RequestParam(value = "page", defaultValue =  "1") int page) {
        Page<Employee> employees = employeeService.fetchEmployees(max, page);
        List<EmployeeResponse> employeeResponseList = employees.getContent().stream()
                .map(EmployeeResponse::new).collect(Collectors.toList());
        return new PageResponse<>(employeeService.fetchTotalEmployee(), page, employeeResponseList);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/employees/list")
    public List<EmployeeListResponse> getListEmployees() {
        List<Employee> employees = employeeService.fetchListEmployee();
        return employees.stream().map(EmployeeListResponse::new).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/employees/manager")
    public EmployeeResponse createManager(@RequestParam(value = "adminId") Long adminId,
                                           @RequestBody EmployeeManagerRequest managerRequest) {
        Employee employee = employeeService.createEmployeeManager(adminId, managerRequest);
        return new EmployeeResponse(employee);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/employees/member")
    public EmployeeResponse createMember(@RequestParam(value = "adminId") Long adminId,
                                         @RequestBody EmployeeMemberRequest memberRequest) {
        Employee employee = employeeService.createEmployeeMember(adminId, memberRequest);
        return new EmployeeResponse(employee);
    }
}
