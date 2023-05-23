package com.linking.global.common;

import com.linking.domain.assign.service.AssignService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Scheduler {

    private final AssignService assignService;

    @PostMapping("/assigns/status/reset")
    public void resetAssignStatus(){
        assignService.setAssignStatus();
    }

}
