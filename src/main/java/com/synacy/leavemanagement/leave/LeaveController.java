package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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
            @RequestParam(value = "page", defaultValue = "1") int page
    ){
        int totalCount = 0;
        Page<Leave> leaves = leaveService.fetchLeaves(max, page);
        List<LeaveResponse> leaveResponsesList = leaves.getContent().stream().map(LeaveResponse::new).collect(Collectors.toList());
        return new PageResponse<>(totalCount, page, leaveResponsesList);
    }


    //TODO: POST Leave
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("api/v1/leave")
    public LeaveResponse createLeave(
            @RequestBody LeaveRequest leaveRequest
    ){
        Leave leave = leaveService.createLeave(leaveRequest);
        return new LeaveResponse(leave);
    }
    //TODO: PUT LeaveStatus (rejected, accepted)

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("api/v1/leave/{id}")
    public LeaveResponse updateLeave(
            @PathVariable Long id,
            @RequestBody LeaveRequest leaveRequest
    ){
        Leave leave = leaveService.updateLeave(id, leaveRequest);

    }
}
