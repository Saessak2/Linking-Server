package com.linking.annotation.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.annotation.persistence.AnnotationRepository;
import com.linking.block.domain.Block;
import com.linking.block.persistence.BlockRepository;
import com.linking.exception.TooManyElementsException;
import com.linking.global.ErrorMessage;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
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
    private final BlockRepository blockRepository;
    private final ParticipantRepository participantRepository;

    public Optional<AnnotationRes> createAnnotation(AnnotationCreateReq req) throws RuntimeException {
        Block block = blockRepository.findById(req.getBlockId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Participant participant = participantRepository.findOneByUserAndProjectId(req.getUserId(), req.getProjectId());
        if (participant == null) {
            throw new NoSuchElementException(ErrorMessage.NO_USER + " or " + ErrorMessage.NO_PROJECT);
        }

        Annotation annotation = annotationMapper.toEntity(req);
        annotation.setBlock(block);
        annotation.setParticipant(participant);

        return Optional.ofNullable(annotationMapper.toDto(annotationRepository.save(annotation)));
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

    public List<AnnotationRes> findAnnotations(Long blockId) {
        Annotation annotation = annotationRepository.findByBlockId(blockId);
        List<AnnotationRes> annotationResList = new ArrayList<>();
        AnnotationRes annotationRes = AnnotationRes.builder()
                .annotationId(annotation.getId())
                .content(annotation.getContent())
                .userName("LeeEunBin")
                .lastModified(annotation.getLastModified().format(DateTimeFormatter.ofPattern("YY-MM-dd HH:mm a")))
                .blockId(annotation.getBlock().getId())
                .build();
        annotationResList.add(annotationRes);
        return annotationResList;
    }
}
