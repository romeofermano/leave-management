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

    Optional<Leave> fetchLeaveId (Long id){
        return leaveRepository.findById(id);
    }

    Page<Leave> fetchLeaves (int max, int page){
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);
        return leaveRepository.findAllByOrderById(pageable);
    }

    Leave createLeave(LeaveRequest leaveRequest){
        Leave leave = new Leave();
        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeService.fetchEmployeeById(leaveRequest.getEmployee_id()));
        Employee employee = optionalEmployee.get();
        leave.setEmployee(employee);
        leave.setStartDate(leaveRequest.getStartDate());
        leave.setEndDate(leaveRequest.getEndDate());
        leave.setDays(leaveRequest.getDays());
        leave.setReason(leaveRequest.getReason());
        leave.setLeaveStatus(LeaveStatus.PENDING);

         return leaveRepository.save(leave);
    }

    Leave updateLeave(Long id, LeaveRequest leaveRequest){
        Optional<Leave> optionalLeave = this.fetchLeaveId(id);

        return new Leave();
    }

    int fetchTotalLeavesCount(){
        return (int) leaveRepository.countAll();
    }
}
