package com.linking.participant;

import com.linking.participant.dto.ParticipantCreateEmailReq;
import com.linking.participant.dto.ParticipantRes;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.NoResultException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @PostMapping
    public ResponseEntity<ParticipantRes> postParticipant(
            @RequestBody @Valid ParticipantCreateEmailReq participantCreateEmailReq){
        try {
            return participantService.createParticipant(participantCreateEmailReq)
                    .map(participantRes -> ResponseEntity.status(HttpStatus.CREATED).body(participantRes))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<ParticipantRes> getParticipant(
            @RequestParam("pt-id") Long participantId){
        try {
            return participantService.getParticipant(participantId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/list")
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
    public ResponseEntity<ParticipantRes> deleteParticipant(@RequestParam("pt-id") Long participantId){
        try{
            return participantService.deleteParticipant(participantId)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build());
        } catch(NoResultException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
