package com.linking.participant;

import com.linking.participant.dto.ParticipantCreateEmailReq;
import com.linking.participant.dto.ParticipantDeleteReq;
import com.linking.participant.dto.ParticipantRes;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/participant")
@CrossOrigin(origins = "*", allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET, RequestMethod.DELETE})
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<ParticipantRes> postParticipant(
            @RequestBody @Valid ParticipantCreateEmailReq participantCreateEmailReq){
        try {
            return participantService.createParticipant(participantCreateEmailReq)
                    .map(participantRes -> ResponseEntity.status(HttpStatus.CREATED).body(participantRes))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch(DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<ParticipantRes> getParticipant(
            @RequestParam("id") Long participantId){
        try {
            return participantService.getParticipant(participantId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/list")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET})
    public ResponseEntity<List<ParticipantRes>> getParticipantList(
            @RequestParam("proj-id") Long projectId){
        try{
            List<ParticipantRes> partList = participantService.getParticipantsByProjectId(projectId);
            if(partList.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return ResponseEntity.ok(partList);
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.DELETE})
    public ResponseEntity<ParticipantRes> deleteParticipant(
            @RequestParam("id") Long participantId){
        try{
            return participantService.deleteParticipant(participantId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/multi")
    @CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST})
    public ResponseEntity<List<ParticipantRes>> deleteParticipants(
            @RequestBody ParticipantDeleteReq participantDeleteReq){
        try{
            List<ParticipantRes> partList = participantService.deleteParticipants(participantDeleteReq);
            if(partList.isEmpty())
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return ResponseEntity.ok(partList);
        } catch(EmptyResultDataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
