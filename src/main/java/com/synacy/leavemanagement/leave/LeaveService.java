package com.synacy.leavemanagement.leave;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveService {
    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    List<Leave> fetchLeaves (int max, int page){
        int offset = page - 1;
        Pageable pageable = PageRequest.of(offset, max);

        return leaveRepository.findAll();
    }
}
