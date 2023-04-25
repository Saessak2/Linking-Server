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
import com.linking.group.controller.DocumentSseHandler;
import com.linking.page.dto.PageIdRes;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.service.ParticipantService;
import com.linking.global.common.WsResType;
import com.linking.ws.event.PageEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnotationService {

    private final DocumentSseHandler documentSseHandler;
    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;
    private final BlockService blockService;
    private final ParticipantService participantService;
    private final PageCheckRepository pageCheckRepository;

    public Map<String, Object> createAnnotation(AnnotationCreateReq req, Long userId) {
        Block block = blockService.getBlock(req.getBlockId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Participant participant = participantService.getParticipant(userId, req.getProjectId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));

        Annotation annotation = annotationMapper.toEntity(req);
        annotation.setBlock(block);
        annotation.setParticipant(participant);

        AnnotationRes annotationRes = annotationMapper.toDto(annotationRepository.save(annotation));

        // 해당 페이지, 참여자(주석생성한 팀원 제외)로 pageCheck 조회하여 annoNotCount 증가시킴
        List<PageCheck> pageCheckList = pageCheckRepository.findAllByPageId(block.getPage().getId());
        pageCheckList.forEach(pc -> {
            if (pc.getParticipant().getParticipantId() != participant.getParticipantId()) {
                pc.increaseAnnotNotCount();
                pageCheckRepository.save(pc);
            }
        });
        // 다른 팀원에게 주석 개수 증가 이벤트 전송
        Map<String, Object> returnVal = new HashMap<>();
        returnVal.put("projectId", req.getProjectId());
        returnVal.put("data", annotationRes);
        returnVal.put("message", new PageIdRes(block.getPage().getId()));

        return returnVal;
    }

    public AnnotationRes updateAnnotation(AnnotationUpdateReq annotationReq, Long userId) {

        Annotation annotation = annotationRepository.findById(annotationReq.getAnnotationId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));

        // 본인이 작성한 주석 아닌 경우 수정 불가
        Participant writer = annotation.getParticipant();
        if ((writer != null && writer.getUser().getUserId() != userId) || (writer == null)) {
            throw new NoAuthorityException("해당 주석을 수정할 권한이 없습니다.");
        }

        annotation.updateContent(annotationReq.getContent());
        AnnotationRes annotationRes = annotationMapper.toDto(annotationRepository.save(annotation));

        return annotationRes;
    }

    public Map<String, Object> deleteAnnotation(Long annotationId, Long projectId, Long userId) {

        Annotation annotation = annotationRepository.findById(annotationId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));

        // 본인이 작성한 주석 아닌 경우 삭제 불가. 주석의 작성자가 탈퇴 또는 프로젝트 나간 경우 삭제 가능
        Participant writer = annotation.getParticipant();
        if (writer != null && writer.getUser().getUserId() != userId) {
            throw new NoAuthorityException("해당 주석을 삭제할 권한이 없습니다.");
        }

        Long pageId = annotation.getBlock().getPage().getId();
        annotationRepository.delete(annotation);

        Participant participant = participantService.getParticipant(userId, projectId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));

        // 해당 페이지, 참여자(주석생성한 팀원 제외)로 pageCheck 조회하여 annoNotCount 감소시킴
        List<PageCheck> pageCheckList = pageCheckRepository.findAllByPageId(pageId);
        pageCheckList.forEach(pc -> {
            if (pc.getParticipant().getParticipantId() != participant.getParticipantId()) {
                pc.reduceAnnoNotCount();
                pageCheckRepository.save(pc);
            }
        });

        // 다른 팀원에게 주석 개수 감소 이벤트 전송
        Map<String, Object> returnVal = new HashMap<>();
        returnVal.put("projectId", projectId);
        returnVal.put("message", new PageIdRes(pageId));

        return returnVal;
    }
}
