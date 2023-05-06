package com.linking.assign.controller;

import com.linking.assign.dto.AssignRes;
import com.linking.assign.dto.AssignStatusUpdateReq;
import com.linking.assign.service.AssignService;
import com.linking.global.common.ResponseHandler;
import com.linking.todo.controller.TodoSseHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/assigns")
@RequiredArgsConstructor
public class AssignController {

    private final AssignService assignService;
    private final TodoSseHandler todoSseHandler;

    @GetMapping("/ratio/project/{id}")
    public ResponseEntity<Object> getAssignsCompletionRatio(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(
                assignService.getAssignCompletionRate(id));
    }

    @PutMapping("/status")
    public ResponseEntity<Object> putAssignStatus(@RequestBody AssignStatusUpdateReq assignStatusUpdateReq){
        Optional<AssignRes> assignRes = assignService.updateAssignStatus(assignStatusUpdateReq);
        if(assignRes.isEmpty())
            return ResponseHandler.generateOkResponse(false);
        todoSseHandler.send(assignStatusUpdateReq.getEmitterId(), "TODO-POSTED", assignRes);
        return ResponseHandler.generateOkResponse(true);
    }

}
