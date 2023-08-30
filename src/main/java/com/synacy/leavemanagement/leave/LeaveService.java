package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.employee.EmployeeRepository;
import com.synacy.leavemanagement.employee.EmployeeService;
import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.web.exceptions.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {
    private final List<Leave> leaveList;
    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;

    private final EmployeeRepository employeeRepository;

    public LeaveService(List<Leave> leaveList, LeaveRepository leaveRepository, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.leaveList = leaveList;
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
        createInitialLeaves();
    }

    private void createInitialLeaves(){
        leaveRepository.saveAll(this.leaveList);
    }
    Optional<Leave> fetchLeaveId(Long id) {
        return leaveRepository.findById(id);
    }

    Page<Leave> fetchLeaves(int max, int page) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);
        return leaveRepository.findAllExceptHRAdmin(RoleType.HR_ADMIN, pageable);
    }

    Page<Leave> fetchLeavesByEmpId(int max, int page, Long employeeId) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return leaveRepository.findAllByEmployee_Id(employeeId, pageable);
    }

    Page<Leave> fetchLeavesUnderManager(int max, int page, Long managerId) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return leaveRepository.findAllByEmployeeManager_IdAndLeaveStatus(managerId, LeaveStatus.PENDING, pageable);
    }

    Optional<Leave> fetchPendingLeave(Long id) {
        return Optional.ofNullable(leaveRepository.findByIdAndLeaveStatus(id, LeaveStatus.PENDING));
    }

    Leave createLeave(LeaveRequest leaveRequest) {

        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeService.fetchEmployeeById(leaveRequest.getEmployee_id()));
        Employee employee = optionalEmployee.get();
        Leave leave = new Leave(employee, leaveRequest.getStartDate(), leaveRequest.getEndDate(), leaveRequest.getReason());
        leave.setLeaveStatus(LeaveStatus.PENDING);
        employee.deductLeave(leave.getDays());
        employeeRepository.save(employee);
        return leaveRepository.save(leave);
    }

    Leave approveLeave(Leave leave) {
        leave.setLeaveStatus(LeaveStatus.APPROVED);

        return leaveRepository.save(leave);
    }

    Leave rejectLeave(Leave leave) {
        leave.setLeaveStatus(LeaveStatus.REJECTED);
        Employee employee = employeeService.fetchEmployeeById(leave.getEmployee().getId());
        employee.addLeave(leave.getDays());
        employeeRepository.save(employee);
        return leaveRepository.save(leave);
    }

    void cancelLeave(Leave leave) {
        leave.cancel();
        Employee employee = employeeService.fetchEmployeeById(leave.getEmployee().getId());
        employee.addLeave(leave.getDays());
        employeeRepository.save(employee);
        leaveRepository.save(leave);
    }

    int fetchTotalLeavesCount() {
        return (int) leaveRepository.countAllBy();
    }

    int fetchTotalLeavesOfEmployeeCount(Long employeeId) {
        return (int) leaveRepository.countAllByEmployee_Id(employeeId);
    }

    int fetchTotalEmployeeLeaveUnderManagerCount(Long managerId) {
        return (int) leaveRepository.countAllByEmployeeManager_IdAndLeaveStatus(managerId, LeaveStatus.PENDING);
    }
}
