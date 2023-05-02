package com.linking.assign.controller;

import com.linking.assign.dto.AssignStatusUpdateReq;
import com.linking.assign.service.AssignService;
import com.linking.global.common.ResponseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/assigns")
@RequiredArgsConstructor
public class AssignController {

    private final AssignService assignService;

    @GetMapping("/ratio/project/{id}")
    public ResponseEntity<Object> getAssignsCompletionRatio(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                assignService.getAssignCompletionRate(id));
    }

    @PutMapping("/status")
    public ResponseEntity<Object> putAssignStatus(@RequestBody AssignStatusUpdateReq assignStatusUpdateReq){
        return ResponseHandler.generateOkResponse(
                assignService.updateAssignStatus(assignStatusUpdateReq));
    }

}
