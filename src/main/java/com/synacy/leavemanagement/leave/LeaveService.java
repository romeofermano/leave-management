package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.model.Employee;
import com.synacy.leavemanagement.services.EmployeeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;

    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
    }

    Page<Leave> fetchLeaves (int max, int page){
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);
        return leaveRepository.findAll(pageable);
    }

    Leave createLeave(LeaveRequest leaveRequest){
        Leave leave = new Leave();
        //TODO: set employee

        leave.setStartDate(leaveRequest.getStartDate());
        leave.setEndDate(leaveRequest.getEndDate());
        leave.setDays(leaveRequest.getDays());
        leave.setReason(leaveRequest.getReason());
        leave.setLeaveStatus(LeaveStatus.PENDING);

        leaveRepository.save(leave);
        return leave;
    }

    Leave updateLeave(Long id, LeaveRequest leaveRequest){
        Optional<Leave> optionalLeave = Optional.ofNullable();

    }
}
