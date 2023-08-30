package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.employee.request.EmployeeRequest;
import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.web.exceptions.InvalidAdminException;
import com.synacy.leavemanagement.web.exceptions.InvalidManagerException;
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
        Optional<Employee> manager = employeeRepository.findByIdAndEmployeeStatusAndRoleTypeIn(id, EmployeeStatus.ACTIVE,
                Arrays.asList(RoleType.MANAGER, RoleType.HR_ADMIN));
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

    public List<Employee> fetchListEmployee(RoleType roleType) {
        if (roleType == RoleType.MANAGER) {
            return employeeRepository.findAllByEmployeeStatusAndRoleType(EmployeeStatus.ACTIVE, RoleType.HR_ADMIN);
        } else if (roleType == RoleType.MEMBER) {
            return employeeRepository.findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus.ACTIVE,
                    Arrays.asList(RoleType.HR_ADMIN, RoleType.MANAGER));
        } else {
            return employeeRepository.findAllByEmployeeStatus(EmployeeStatus.ACTIVE);
        }
    }

    public Employee createEmployee(Long adminId, EmployeeRequest employeeRequest) {
        Optional<Employee> employeeOptional = getEmployeeAdminById(adminId);
        if (employeeOptional.isPresent() && employeeOptional.get().getRoleType() == RoleType.HR_ADMIN) {
            Employee manager = getEmployeeManagerById(employeeRequest.getManagerId());
            if (employeeRequest.getRoleType() == manager.getRoleType()) {
                throw new InvalidManagerException("Cannot create an employee with the same role as the manager.");
            } else {
                Employee employee = new Employee(employeeRequest.getName(), employeeRequest.getRoleType(),
                        employeeRequest.getTotalLeaves(), getEmployeeManagerById(employeeRequest.getManagerId()));

                employeeRepository.save(employee);
                return employee;
            }
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
