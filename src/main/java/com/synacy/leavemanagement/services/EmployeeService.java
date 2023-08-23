package com.synacy.leavemanagement.services;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.repository.EmployeeRepository;
import com.synacy.leavemanagement.request.EmployeeManagerRequest;
import com.synacy.leavemanagement.request.EmployeeMemberRequest;
import com.synacy.leavemanagement.web.exceptions.InvalidAdminException;
import com.synacy.leavemanagement.web.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private Optional<Employee> findEmployeeAdminById(Long id) {
        return employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN);
    }

    private Employee getManagerById(Long id) {
        Optional<Employee> manager = employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE,
                RoleType.MANAGER);
        return manager.orElseThrow(() -> new UserNotFoundException("Manager not found"));
    }

    public Integer fetchTotalEmployee() {
        return employeeRepository.countAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                Arrays.asList(RoleType.MEMBER, RoleType.MANAGER));
    }

    public Employee fetchEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findByIdAndEmployeeStatus(id, EmployeeStatus.ACTIVE);
        return employee.orElseThrow(() -> new UserNotFoundException("Employee not found"));
    }

    public Page<Employee> fetchEmployees(int max, int page) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                Arrays.asList(RoleType.MANAGER, RoleType.MEMBER), pageable);
    }

    public List<Employee> fetchListEmployee() {
        return employeeRepository.findAllByEmployeeStatus(EmployeeStatus.ACTIVE);
    }

    // TODO: List all employees with the employee status is active
    // TODO: List all manager
    // TODO: Create new employees
    // TODO: Update existing employees
    // TODO: Terminate employees

    public Employee createEmployeeManager(Long adminId, EmployeeManagerRequest managerRequest) {
        Optional<Employee> employeeOptional = findEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(managerRequest.getName(), managerRequest.getRoleType(),
                    managerRequest.getTotalLeaves(), employeeOptional.get());

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admins can create new employee");
    }

    public Employee createEmployeeMember(Long adminId, EmployeeMemberRequest memberRequest) {
        Optional<Employee> employeeOptional = findEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(memberRequest.getName(), memberRequest.getRoleType(),
                    memberRequest.getTotalLeaves(), getManagerById(memberRequest.getManagerId()));

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admin can create new employee");
    }

    public Employee updateEmployeeManager(Long adminId, EmployeeManagerRequest managerRequest) {
        return null;
    }
}
