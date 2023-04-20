package com.linking.annotation.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.AnnotationCreateReq;
import com.linking.annotation.dto.AnnotationRes;
import com.linking.annotation.dto.AnnotationUpdateReq;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.annotation.persistence.AnnotationRepository;
import com.linking.block.domain.Block;
import com.linking.block.service.BlockService;
import com.linking.global.exception.NoAuthorityException;
import com.linking.global.message.ErrorMessage;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.service.ParticipantService;
import com.linking.global.common.WsResType;
import com.linking.ws.event.PageEvent;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AnnotationService {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ApplicationEventPublisher publisher;
    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;
    private final BlockService blockService;
    private final ParticipantService participantService;
    private final PageCheckRepository pageCheckRepository;
    PageEvent.PageEventBuilder pageEvent = PageEvent.builder();

    public AnnotationRes createAnnotation(AnnotationCreateReq req, Long userId) {
        Block block = blockService.getBlock(req.getBlockId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Participant participant = participantService.getParticipant(req.getUserId(), req.getProjectId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));

        Annotation annotation = annotationMapper.toEntity(req);
        annotation.setBlock(block);
        annotation.setParticipant(participant);

        AnnotationRes annotationRes = annotationMapper.toDto(annotationRepository.save(annotation));

        // 주석 생성 이벤트 발행
        publisher.publishEvent(
                pageEvent
                        .resType(WsResType.CREATE_ANNO)
                        .pageId(block.getPage().getId())
                        .userId(userId)
                        .data(annotationRes).build()
        );


        // 주석 알림 개수 이벤트 발행
//        PageCheckEvent.PageCheckEventBuilder pageCheckEvent = PageCheckEvent.builder();
//        publisher.publishEvent(
//                pageCheckEvent
//                        .resType(WsResType.CREATE_ANNO)
//                        .pageId(block.getPage().getId())
//                        .data(pageCheckRepository.findAllByPageId(block.getPage().getId()))
//        );

        // pageHandler에 이벤트로 pageCheckList 보냄. pageHandler에서 현재 페이지 접속 중인 userId목록을 wsPageService를 통해 pageCheckService로 보냄
        // pageCheckService에서 받은 userid목록 제외한 팀원들의 annoCnt를 증가시킴.
        // documentHandler에 이벤트로 annoCntList(userId목록 제외한 팀원들의)를 보냄. 현재 문서리스트 조회중인 사용자들에서 메시지 전송

        return annotationRes;
    }

    public AnnotationRes updateAnnotation(AnnotationUpdateReq annotationReq, Long userId) {
        Annotation annotation = annotationRepository.findById(annotationReq.getAnnotationId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));

        annotation.updateContent(annotationReq.getContent());
        AnnotationRes annotationRes = annotationMapper.toDto(annotationRepository.save(annotation));

        // 이벤트 발행
        publisher.publishEvent(
                pageEvent
                        .resType(WsResType.UPDATE_ANNO)
                        .pageId(annotation.getBlock().getPage().getId())
                        .userId(userId)
                        .data(annotationRes).build()
        );

        return annotationRes;
    }

    public void deleteAnnotation(Long annotationId, Long userId) {
        Annotation annotation = annotationRepository.findById(annotationId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));
        // 본인이 작성한 주석 아닌 경우 삭제 불가
        Long writerId = annotation.getParticipant().getUser().getUserId();
        if (writerId != null) // 작성인(사용자)이 탈퇴한 경우는 삭제 가능
            if (writerId != userId)
                throw new NoAuthorityException("해당 주석을 삭제할 권한이 없습니다.");

        Long pageId = annotation.getBlock().getPage().getId();
        annotationRepository.delete(annotation);
        // 이벤트 발행
        publisher.publishEvent(
                pageEvent
                        .resType(WsResType.DELETE_ANNO)
                        .pageId(pageId)
                        .userId(userId)
                        .data(annotationId).build()
        );
    }
}
