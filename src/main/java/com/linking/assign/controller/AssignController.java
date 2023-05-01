package com.linking.assign.controller;

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


    // 할 일 상태 수정 -> assign
    @PutMapping("/{id}")
    public ResponseEntity<Object> putTodoStatus(@PathVariable Long id, @RequestBody String status){
        return ResponseHandler.generateOkResponse(
                assignService.updateAssignStatus(id, status));
    }

}
