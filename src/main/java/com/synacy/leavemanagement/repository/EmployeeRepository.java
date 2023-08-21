package com.synacy.leavemanagement.repository;

import com.synacy.leavemanagement.enums.EmployeeStatus;
import com.synacy.leavemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    public Page<Employee> findAllByEmployeeStatus(EmployeeStatus employeeStatus, Pageable pageable);
}
