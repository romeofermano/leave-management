package com.synacy.leavemanagement.services;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.repository.EmployeeRepository;
import com.synacy.leavemanagement.request.EmployeeRequest;
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

    public Employee getEmployeeById() {
        return null;
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

    public Employee createEmployee(Long adminId, EmployeeRequest employeeRequest) {
        Optional<Employee> employeeOptional = findEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(employeeRequest.getName(), employeeRequest.getRoleType(),
                    employeeRequest.getTotalLeaves(), getManagerById(employeeRequest.getManagerId()));

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admins can create new members");
    }

    public Employee updateMember(Long adminId, EmployeeRequest employeeRequest) {
        return null;
    }
}
