package com.synacy.leavemanagement.services;

import com.synacy.leavemanagement.request.EmployeeManagerRequest;
import com.synacy.leavemanagement.request.EmployeeMemberRequest;
import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.repository.EmployeeRepository;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.web.exceptions.InvalidAdminException;
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

    private Optional<Employee> findEmployeeAdminById(Long id) {
        return employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN);
    }

    private Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
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

    public Employee createMember(Long adminId, EmployeeMemberRequest employeeMemberRequest) {
        Optional<Employee> employeeOptional = findEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(employeeMemberRequest.getName(), employeeMemberRequest.getRoleType(),
                    employeeMemberRequest.getTotalLeaves());;

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admins can create new members");
    }

    public Employee createManager(Long adminId, EmployeeManagerRequest employeeManagerRequest) {
        Optional<Employee> employeeOptional = findEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(employeeManagerRequest.getName(), employeeManagerRequest.getRoleType(),
                    employeeManagerRequest.getTotalLeaves());

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admins can create new members");
    }

    public Employee updateMember(Long adminId, EmployeeMemberRequest employeeMemberRequest) {
        return null;
    }
}
