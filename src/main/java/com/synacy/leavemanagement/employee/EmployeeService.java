package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.employee.request.EmployeeManagerRequest;
import com.synacy.leavemanagement.employee.request.EmployeeMemberRequest;
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

    private Optional<Employee> getEmployeeAdminById(Long id) {
        return employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE, RoleType.HR_ADMIN);
    }

    private Employee getEmployeeManagerById(Long id) {
        Optional<Employee> manager = employeeRepository.findByIdAndEmployeeStatusAndRoleType(id, EmployeeStatus.ACTIVE,
                RoleType.MANAGER);
        return manager.orElseThrow(() -> new UserNotFoundException("Manager not found"));
    }

    public Integer fetchTotalEmployee() {
        return employeeRepository.countAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                Arrays.asList(RoleType.MEMBER, RoleType.MANAGER));
    }

    public Employee fetchEmployeeById(Long id) {
        Optional<Employee> employee = employeeRepository.findByIdAndEmployeeStatusAndRoleTypeIn(id,
                EmployeeStatus.ACTIVE, Arrays.asList(RoleType.MEMBER, RoleType.MANAGER));
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
        Optional<Employee> employeeOptional = getEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(managerRequest.getName(), managerRequest.getRoleType(),
                    managerRequest.getTotalLeaves(), employeeOptional.get());

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admins can create new employee");
    }

    public Employee createEmployeeMember(Long adminId, EmployeeMemberRequest memberRequest) {
        Optional<Employee> employeeOptional = getEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = new Employee(memberRequest.getName(), memberRequest.getRoleType(),
                    memberRequest.getTotalLeaves(), getEmployeeManagerById(memberRequest.getManagerId()));

            employeeRepository.save(employee);
            return employee;
        }
        throw new InvalidAdminException("Only HR Admin can create new employee");
    }

    public void terminateEmployee(Long adminId, Long employeeId) {
        Optional<Employee> adminOptional = getEmployeeAdminById(adminId);
        if (adminOptional.isPresent() && adminOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee employee = fetchEmployeeById(employeeId);
            employee.terminate();
            employeeRepository.save(employee);
        } else {
            throw new InvalidAdminException("Only HR Admin can terminate employee");
        }
    }
}
