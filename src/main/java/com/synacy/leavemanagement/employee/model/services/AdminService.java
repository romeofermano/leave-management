package com.synacy.leavemanagement.employee.model.services;

import com.synacy.leavemanagement.employee.model.request.EmployeeRequest;
import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.employee.model.Employee;
import com.synacy.leavemanagement.employee.model.repository.EmployeeRepository;
import com.synacy.leavemanagement.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

public class AdminService {
    private final EmployeeRepository employeeRepository;

    public AdminService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private Optional<Employee> findAdminById(Long id) {
        return employeeRepository.findByIdAndRoleType(id, RoleType.HR_ADMIN);
    }

    public Page<Employee> fetchEmployees(int max, int page) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                Arrays.asList(RoleType.MANAGER, RoleType.MEMBER), pageable);
    }

    // TODO: List all employees with the employee status is active
    // TODO: List all manager
    // TODO: Create new employees
    // TODO: Update existing employees
    // TODO: Terminate employees

    public Employee createMember(Long adminId, EmployeeRequest employeeRequest) {
        Optional<Employee> employeeOptional = findAdminById(adminId);
        if (employeeOptional.isPresent()) {
            Employee employee = new Employee(employeeRequest.getName(), employeeRequest.getRoleType(),
                    employeeRequest.getTotalLeaves());;

            employeeRepository.save(employee);
            return employee;
        }
        return null;
    }
}
