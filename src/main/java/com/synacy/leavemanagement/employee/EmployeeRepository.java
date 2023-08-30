package com.synacy.leavemanagement.employee;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.enums.RoleType;
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

    List<Employee> findAllByEmployeeStatus(EmployeeStatus employeeStatus);

    List<Employee> findAllByEmployeeStatusAndRoleType(EmployeeStatus employeeStatus, RoleType roleType);

    List<Employee> findAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus employeeStatus, Collection<RoleType> roleType);

    Optional<Employee> findByIdAndEmployeeStatusAndRoleTypeIn(Long id, EmployeeStatus employeeStatus, Collection<RoleType> roleType);

    int countAllByEmployeeStatusAndRoleTypeIn(EmployeeStatus employeeStatus, Collection<RoleType> roleType);

    Optional<Employee> findByIdAndEmployeeStatusAndRoleType(Long id, EmployeeStatus employeeStatus, RoleType roleType);
}
