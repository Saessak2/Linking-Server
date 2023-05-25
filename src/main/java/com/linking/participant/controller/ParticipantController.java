package com.linking.participant.controller;

import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantSimplifiedRes;
import com.linking.global.common.ResponseHandler;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/new")
    public ResponseEntity<Object> postParticipant(
            @RequestBody @Valid ParticipantIdReq participantIdReq
    ){
        return participantService.createParticipant(participantIdReq)
                .map(p -> ResponseHandler.generateResponse(ResponseHandler.MSG_201, HttpStatus.CREATED, p))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getParticipant(
            @PathVariable Long id
    ){
        return participantService.getParticipant(id)
                .map(p -> ResponseHandler.generateResponse(ResponseHandler.MSG_200, HttpStatus.OK, p))
                .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
    }

    @GetMapping("/list/project/{id}")
    public ResponseEntity<Object> getParticipantList(
            @PathVariable("id") Long projectId
    ){
        List<ParticipantSimplifiedRes> participantList = participantService.getParticipantsByProjectId(projectId);
        if(participantList.isEmpty())
            return ResponseHandler.generateInternalServerErrorResponse();
        return ResponseHandler.generateOkResponse(participantList);
    }

    @PostMapping
    public ResponseEntity<Object> deleteParticipants(
            @RequestBody ParticipantDeleteReq participantDeleteReq
    ){
        participantService.deleteParticipant(participantDeleteReq);
        return ResponseHandler.generateNoContentResponse();
    }

}
