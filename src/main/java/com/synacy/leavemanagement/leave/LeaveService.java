package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.employee.Employee;
import com.synacy.leavemanagement.employee.EmployeeRepository;
import com.synacy.leavemanagement.employee.EmployeeService;
import com.synacy.leavemanagement.enums.LeaveStatus;
import com.synacy.leavemanagement.enums.RoleType;
import com.synacy.leavemanagement.web.exceptions.DateException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class LeaveService {
    private final LeaveRepository leaveRepository;
    private final EmployeeService employeeService;

    private final EmployeeRepository employeeRepository;

    public LeaveService(LeaveRepository leaveRepository, EmployeeService employeeService, EmployeeRepository employeeRepository) {
        this.leaveRepository = leaveRepository;
        this.employeeService = employeeService;
        this.employeeRepository = employeeRepository;
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

        return leaveRepository.findAllByEmployee_IdOrderByIdDesc(employeeId, pageable);
    }

    Page<Leave> fetchLeavesUnderManager(int max, int page, Long managerId) {
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return leaveRepository.findLeavesByManagerIdExcludingManagerLeaves(managerId, pageable);
    }

    Optional<Leave> fetchPendingLeave(Long id) {
        return Optional.ofNullable(leaveRepository.findByIdAndLeaveStatus(id, LeaveStatus.PENDING));
    }

    Leave createLeave(LeaveRequest leaveRequest) {
        Optional<Employee> optionalEmployee = Optional.ofNullable(employeeService.fetchEmployeeById(leaveRequest.getEmployee_id()));
        Employee employee = optionalEmployee.get();
        Leave leave = new Leave(employee, leaveRequest.getStartDate(), leaveRequest.getEndDate(), leaveRequest.getReason());
        leave.setLeaveStatus(LeaveStatus.PENDING);

        LocalDate today = LocalDate.now();
        if (leave.getDays() > employee.getCurrentLeaves()) {
            throw new DateException(
                    "Number of days filed in leave exceeds current leave credits"
            );
        } else if (leaveRequest.startDate.isBefore(today) || leaveRequest.endDate.isBefore(today)) {
            throw new DateException(
                    "Cannot file leave with dates before current date"
            );
        } else if (leaveRequest.endDate.isBefore(leaveRequest.startDate)) {
            throw new DateException(
                    "Cannot file leave with end date earlier than start date"
            );
        } else {
            employee.deductLeave(leave.getDays());
            employeeRepository.save(employee);
            return leaveRepository.save(leave);
        }
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
