package com.synacy.leavemanagement.repository;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus employeeStatus, List<RoleType> roleTypes,
                                                        Pageable pageable);
    Optional<Employee> findByIdAndEmployeeStatusAndRoleType(Long id, EmployeeStatus employeeStatus, RoleType roleType);
    Optional<Employee> findByIdAndEmployeeStatus(Long id, EmployeeStatus employeeStatus);
}
