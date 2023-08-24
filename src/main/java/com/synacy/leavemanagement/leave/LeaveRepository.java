package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.model.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Page<Leave> findAllByOrderById(Pageable pageable);

    @Query("SELECT l FROM Leave l WHERE l.employee IN" +
            "(SELECT e FROM Employee e WHERE e.roleType <> :roleType)")
    Page<Leave> findAllExceptHRAdmin(RoleType roleType, Pageable pageable);

    Page<Leave> findAllByEmployee_Id(Long id, Pageable pageable);

    Page<Leave> findAllByEmployeeManager_IdAndLeaveStatus(Long id, LeaveStatus leaveStatus, Pageable pageable);

    Leave findByIdAndLeaveStatus(Long id, LeaveStatus leaveStatus);
    long countAllBy();

    long countAllByEmployee_Id(Long id);

    long countAllByEmployeeManager_IdAndLeaveStatus(Long employee_id, LeaveStatus leaveStatus);
}
