package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    //TODO: Add exceptions
    @GetMapping("api/v1/leave")
    public PageResponse<LeaveResponse> fetchLeaves(
            @RequestParam(value = "max", defaultValue = "3") int max,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "employeeId", required = false) Long employeeId
    ) {
        int totalCount;
        Page<Leave> leaves;

        if (employeeId != null) {
            leaves = leaveService.fetchLeavesByEmpId(max, page, employeeId);
            totalCount = leaveService.fetchTotalLeavesOfEmployeeCount(employeeId);
        } else {
            leaves = leaveService.fetchLeaves(max, page);
            totalCount = leaveService.fetchTotalLeavesCount();
        }
        List<LeaveResponse> leaveResponsesList = leaves.getContent().stream().map(LeaveResponse::new).collect(Collectors.toList());
        return new PageResponse<>(totalCount, page, leaveResponsesList);
    }

    @GetMapping("api/v1/leave/manager/{id}")
    public PageResponse<LeaveResponse> fetchLeavesUnderManager(
            @RequestParam(value = "max", defaultValue = "3") int max,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @PathVariable Long id
    ) {
        int totalCount = leaveService.fetchTotalLeavesCount();
        Page<Leave> leaves = leaveService.fetchLeavesUnderManager(max, page, id);
        List<LeaveResponse> leaveResponseList = leaves.getContent().stream().map(LeaveResponse::new).toList();
        return new PageResponse<>(totalCount, page, leaveResponseList);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/leave")
    public LeaveResponse createLeave(
            @RequestBody LeaveRequest leaveRequest
    ) {
        Leave leave = leaveService.createLeave(leaveRequest);
        return new LeaveResponse(leave);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("api/v1/leave/approve/{id}")
    public LeaveResponse approveLeave(
            @PathVariable Long id
    ) {
        Leave leave = leaveService.approveLeave(id);
        return new LeaveResponse(leave);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("api/v1/leave/reject/{id}")
    public LeaveResponse rejectLeave(
            @PathVariable Long id
    ) {
        Leave leave = leaveService.rejectLeave(id);
        return new LeaveResponse(leave);
    }
}
