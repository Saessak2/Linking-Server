package com.linking.annotation.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.annotation.persistence.AnnotationRepository;
import com.linking.block.domain.Block;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.ErrorMessage;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class    AnnotationService {
    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;
    private final BlockRepository blockRepository;
    private final ParticipantRepository participantRepository;

    public AnnotationRes createAnnotation(AnnotationCreateReq annotationReq) throws Exception {
        Block refBlock = blockRepository.getReferenceById(annotationReq.getBlockId());
        Participant refParticipant = participantRepository.getReferenceById(annotationReq.getParticipantId());

        Annotation annotation = annotationMapper.toEntity(annotationReq);
        annotation.setBlock(refBlock);
        annotation.setParticipant(refParticipant);

        return annotationMapper.toDto(annotationRepository.save(annotation));
    }

    public AnnotationRes updateAnnotation(AnnotationUpdateReq annotationReq) {
        Annotation findAnnotation = annotationRepository.findById(annotationReq.getAnnotationId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));

        findAnnotation.updateContent(annotationReq.getContent());

        return annotationMapper.toDto(annotationRepository.save(findAnnotation));
    }

    public void deleteAnnotation(Long annotationId) {

        annotationRepository.delete(
                annotationRepository.findById(annotationId).orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION))
        );
    }
}
