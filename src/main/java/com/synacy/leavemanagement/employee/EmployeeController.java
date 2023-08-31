package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.employee.request.EmployeeRequest;
import com.synacy.leavemanagement.employee.response.EmployeeListResponse;
import com.synacy.leavemanagement.employee.response.EmployeeResponse;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.web.exceptions.response.PageResponse;
import jakarta.validation.Valid;
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
    @GetMapping("/api/v1/employees")
    public PageResponse<EmployeeResponse> getEmployees(@RequestParam(value = "max", defaultValue = "10") int max,
                                                       @RequestParam(value = "page", defaultValue = "1") int page) {
        Page<Employee> employees = employeeService.fetchEmployees(max, page);
        List<EmployeeResponse> employeeResponseList = employees.getContent().stream()
                .map(EmployeeResponse::new).collect(Collectors.toList());
        return new PageResponse<>(employeeService.fetchTotalEmployee(), page, employeeResponseList);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/employees/list")
    public List<EmployeeListResponse> getListEmployees(@RequestParam(value = "roleType", defaultValue = "",
            required = false) RoleType roleType) {
        List<Employee> employees = employeeService.fetchListEmployee(roleType);
        return employees.stream().map(EmployeeListResponse::new).collect(Collectors.toList());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/v1/employees/{id}")
    public EmployeeResponse getEmployee(@PathVariable Long id){
        Employee employee = this.employeeService.fetchEmployeeById(id);
        return new EmployeeResponse(employee);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/v1/employees")
    public EmployeeResponse createEmployee(@RequestParam(value = "adminId") Long adminId,
                                           @Valid @RequestBody EmployeeRequest employeeRequest) {
        Employee employee = employeeService.createEmployee(adminId, employeeRequest);
        return new EmployeeResponse(employee);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/v1/employees/{employeeId}")
    public void terminateEmployee(@RequestParam(value = "adminId") Long adminId, @PathVariable Long employeeId) {
        employeeService.terminateEmployee(adminId, employeeId);
    }
}
