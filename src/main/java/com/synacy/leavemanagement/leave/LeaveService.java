package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.web.exceptions.ResourceNotFoundException;
import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.employee.EmployeeService;
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

    Page<Leave> fetchLeavesByEmpId (int max, int page, Long employeeId){
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return leaveRepository.findAllByEmployee_Id(employeeId, pageable);
    }

    Page<Leave> fetchLeavesUnderManager (int max, int page, Long managerId){
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return leaveRepository.findAllByEmployeeManager_IdAndLeaveStatus(managerId, LeaveStatus.PENDING, pageable);
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

    Leave approveLeave(Long id){
        Leave leave = fetchLeaveId(id).orElseThrow(ResourceNotFoundException::new);
        leave.setLeaveStatus(LeaveStatus.APPROVED);
        return leaveRepository.save(leave);
    }

    Leave rejectLeave(Long id){
        Leave leave = fetchLeaveId(id).orElseThrow(ResourceNotFoundException::new);
        leave.setLeaveStatus(LeaveStatus.REJECTED);
        return leaveRepository.save(leave);
    }

    int fetchTotalLeavesCount(){
        return (int) leaveRepository.countAllBy();
    }

    int fetchTotalLeavesOfEmployeeCount(Long employeeId){
        return (int) leaveRepository.countAllByEmployee_Id(employeeId);
    }
}
