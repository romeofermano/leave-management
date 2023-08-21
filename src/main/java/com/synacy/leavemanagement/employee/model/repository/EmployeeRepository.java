package com.synacy.leavemanagement.employee.model.repository;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.employee.model.Employee;
import com.synacy.leavemanagement.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus employeeStatus, List<RoleType> roleTypes,
                                                        Pageable pageable);
    Optional<Employee> findByIdAndRoleType(Long id, RoleType roleType);
}
