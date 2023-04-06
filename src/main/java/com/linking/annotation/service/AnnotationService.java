package com.linking.annotation.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.annotation.persistence.AnnotationRepository;
import com.linking.block.domain.Block;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.ErrorMessage;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
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
    private final UserRepository userRepository;

    public Optional<AnnotationRes> createAnnotation(AnnotationCreateReq req) throws Exception {
        Block refBlock = blockRepository.getReferenceById(req.getBlockId());
        // TODO participant 조회 하면서 user랑 조인해서 username 같이 가져오기
        participantRepository.findByUser(req.getUserId());

        Annotation annotation = annotationMapper.toEntity(req);
        annotation.setBlock(refBlock);
        annotation.setParticipant(refParticipant);

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
}
