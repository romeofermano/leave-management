package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Page<Leave> findAllByOrderById(Pageable pageable);

    Page<Leave> findAllByEmployee_Id(Long id, Pageable pageable);

    Page<Leave> findAllByEmployeeManager_IdAndLeaveStatus(Long id, LeaveStatus leaveStatus, Pageable pageable);
    long countAllBy();

    long countAllByEmployee_Id(Long id);

    long countAllByEmployeeManager_IdAndLeaveStatus(Long employee_id, LeaveStatus leaveStatus);
}
