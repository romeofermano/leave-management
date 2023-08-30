package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.enums.RoleType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Page<Leave> findAllByOrderById(Pageable pageable);

    @Query("SELECT l FROM Leave l WHERE l.employee IN" +
            "(SELECT e FROM Employee e WHERE e.roleType <> :roleType)")
    Page<Leave> findAllExceptHRAdmin(RoleType roleType, Pageable pageable);

    Page<Leave> findAllByEmployee_Id(Long id, Pageable pageable);

    @Query("SELECT l FROM Leave l JOIN l.employee e " +
            "WHERE e.manager.id = :managerId " +
            "AND e.id <> :managerId " +
            "AND l.leaveStatus = 'PENDING'")
    Page<Leave> findPendingLeavesByManagerIdExcludingManagerLeaves(@Param("managerId") Long managerId, Pageable pageable);

    Page<Leave> findAllByEmployeeManager_IdAndLeaveStatus(Long id, LeaveStatus leaveStatus, Pageable pageable);

    Leave findByIdAndLeaveStatus(Long id, LeaveStatus leaveStatus);

    long countAllBy();

    long countAllByEmployee_Id(Long id);

    long countAllByEmployeeManager_IdAndLeaveStatus(Long employee_id, LeaveStatus leaveStatus);
}
