package com.synacy.leavemanagement.repository;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Page<Employee> findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus employeeStatus, Collection<RoleType> roleTypes,
                                                        Pageable pageable);
    Optional<Employee> findByIdAndEmployeeStatusAndRoleType(Long id, EmployeeStatus employeeStatus, RoleType roleType);
    List<Employee> findAllByEmployeeStatus(EmployeeStatus employeeStatus);
    int countAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus employeeStatus, Collection<RoleType> roleType);
}
