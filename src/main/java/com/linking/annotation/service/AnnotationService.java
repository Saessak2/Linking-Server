package com.linking.annotation.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.annotation.persistence.AnnotationRepository;
import com.linking.block.domain.Block;
import com.linking.block.persistence.BlockRepository;
import com.linking.block.service.BlockService;
import com.linking.exception.TooManyElementsException;
import com.linking.global.ErrorMessage;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.participant.service.ParticipantService;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class    AnnotationService {
    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;
    private final BlockService blockService;
    private final ParticipantService participantService;

    public AnnotationRes createAnnotation(AnnotationCreateReq req) {
        Block block = blockService.getBlock(req.getBlockId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Participant participant = participantService.getParticipant(req.getUserId(), req.getProjectId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));

        Annotation annotation = annotationMapper.toEntity(req);
        annotation.setBlock(block);
        annotation.setParticipant(participant);

        return annotationMapper.toDto(annotationRepository.save(annotation));
    }

    public AnnotationRes updateAnnotation(AnnotationUpdateReq annotationReq) {
        Annotation annotation = annotationRepository.findById(annotationReq.getAnnotationId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));

        annotation.updateContent(annotationReq.getContent());

        return annotationMapper.toDto(annotationRepository.save(annotation));
    }

    public void deleteAnnotation(Long annotationId) {
        annotationRepository.delete(
                annotationRepository.findById(annotationId).orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION))
        );
    }
}
