package com.synacy.leavemanagement.leave;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave, Long> {
    Page<Leave> findAllByOrderById(Pageable pageable);

    long countAll();
}
