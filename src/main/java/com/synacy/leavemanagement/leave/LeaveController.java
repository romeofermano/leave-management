package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.PageResponse;
import com.synacy.leavemanagement.web.exceptions.InvalidPaginationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import com.synacy.leavemanagement.web.exceptions.ResourceNotFoundException;

@RestController
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/leave/hr")
    public PageResponse<LeaveResponse> fetchAllLeaves(
            @RequestParam(value = "max", defaultValue = "5") int max,
            @RequestParam(value = "page", defaultValue = "1") int page
    ){

        if(max < 1 || page < 1){
            throw new InvalidPaginationException(
                    "INVALID_PAGINATION", "Invalid pagination parameters. Max or Page cannot be less than 1."
            );
        }

        Page<Leave> leaves = leaveService.fetchLeaves(max, page);
        int totalCount = leaveService.fetchTotalLeavesCount();

        List<LeaveResponse> leaveResponsesList = leaves.getContent().stream().map(LeaveResponse::new).collect(Collectors.toList());
        return new PageResponse<>(totalCount, page, leaveResponsesList);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/leave")
    public PageResponse<LeaveResponse> fetchEmployeeLeaves(
            @RequestParam(value = "max", defaultValue = "5") int max,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "employeeId") Long employeeId
    ) {
        int totalCount;
        Page<Leave> leaves;
        if(max < 1 || page < 1){
            throw new InvalidPaginationException(
                    "INVALID_PAGINATION", "Invalid pagination parameters. Max or Page cannot be less than 1."
            );
        }
        leaves = leaveService.fetchLeavesByEmpId(max, page, employeeId);
        totalCount = leaveService.fetchTotalLeavesOfEmployeeCount(employeeId);

        List<LeaveResponse> leaveResponsesList = leaves.getContent().stream().map(LeaveResponse::new).collect(Collectors.toList());
        return new PageResponse<>(totalCount, page, leaveResponsesList);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("api/v1/leave/manager")
    public PageResponse<LeaveResponse> fetchLeavesUnderManager(
            @RequestParam(value = "max", defaultValue = "5") int max,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "managerId") Long managerId
    ){
        if(max < 1 || page < 1){
            throw new InvalidPaginationException(
                    "INVALID_PAGINATION", "Invalid pagination parameters. Max or Page cannot be less than 1."
            );
        }
        int totalCount = leaveService.fetchTotalEmployeeLeaveUnderManagerCount(managerId);
        Page<Leave> leaves = leaveService.fetchLeavesUnderManager(max, page, managerId);
        List<LeaveResponse> leaveResponseList = leaves.getContent().stream().map(LeaveResponse::new).toList();
        return new PageResponse<>(totalCount, page, leaveResponseList);
    }


    @Transactional
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
        Leave leave = leaveService.fetchLeaveId(id).orElseThrow(ResourceNotFoundException::new);
        leaveService.approveLeave(leave);
        return new LeaveResponse(leave);
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("api/v1/leave/reject/{id}")
    public LeaveResponse rejectLeave(
            @PathVariable Long id
    ) {
        Leave leave = leaveService.fetchLeaveId(id).orElseThrow(ResourceNotFoundException::new);
        leaveService.rejectLeave(leave);
        return new LeaveResponse(leave);
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("api/v1/leave/{id}")
    public void cancelLeave(
            @PathVariable Long id
    ){
        Leave leave = leaveService.fetchPendingLeave(id).orElseThrow(ResourceNotFoundException::new);
        leaveService.cancelLeave(leave);
    }
}
