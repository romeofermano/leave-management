package com.synacy.leavemanagement.leave;

import com.synacy.leavemanagement.PageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class LeaveController {

    private final LeaveService leaveService;

    @Autowired
    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    @GetMapping("api/v1/leave")
    public PageResponse<LeaveResponse> fetchLeaves(
            @RequestParam(value = "max", defaultValue = "3") int max,
            @RequestParam(value = "page", defaultValue = "1") int page
    ){
        PageResponse<LeaveResponse> leaves = leaveService.fetchLeaves(max, page);
    }

    //TODO: GET Leave (params: id, date, reason, status)
    //TODO: POST Leave
    //TODO: DELETE Leave (?)
    //TODO: PUT LeaveStatus (rejected, accepted)
}
