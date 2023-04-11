package com.linking.participant.controller;

import com.linking.global.ResponseHandler;
import com.linking.participant.dto.ParticipantIdReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import com.linking.participant.service.ParticipantService;
import com.linking.project.dto.ProjectRes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/participants")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping("/new")
    public ResponseEntity<Object> postParticipant(
            @RequestBody @Valid ParticipantIdReq participantIdReq){
        try {
            return participantService.createParticipant(participantIdReq)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(DuplicateKeyException e){
            return ResponseHandler.generateBadRequestResponse();
        } catch(DataIntegrityViolationException e) {
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @PostMapping("/{id}")
    public ResponseEntity<Object> getParticipant(
            @PathVariable("id") Long participantId){
        try {
            return participantService.getParticipant(participantId)
                    .map(ResponseHandler::generateOkResponse)
                    .orElseGet(ResponseHandler::generateInternalServerErrorResponse);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @PostMapping("/list/{id}")
    public ResponseEntity<Object> getParticipantList(
            @PathVariable("id") Long projectId){
        try{
            List<ParticipantRes> participantList = participantService.getParticipantsByProjectId(projectId);
            if(participantList.isEmpty())
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateOkResponse(participantList);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @PostMapping("/my-list/{id}")
    public ResponseEntity<Object> getParticipantMyList(
            @PathVariable("id") Long userId){
        try{
            List<ProjectRes> participantList = participantService.getPartsByUserId(userId);
            if(participantList.isEmpty())
                return ResponseHandler.generateInternalServerErrorResponse();
            return ResponseHandler.generateOkResponse(participantList);
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        }
    }

    @PostMapping
    public ResponseEntity<Object> deleteParticipants(
            @RequestBody ParticipantDeleteReq participantDeleteReq){
        try{
            participantService.deleteParticipant(participantDeleteReq);
            return ResponseHandler.generateNoContentResponse();
        } catch(NoSuchElementException e){
            return ResponseHandler.generateNotFoundResponse();
        } catch(SQLIntegrityConstraintViolationException e){
            return ResponseHandler.generateBadRequestResponse();
        }
    }

}
