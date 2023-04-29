package com.linking.annotation.service;

import com.linking.annotation.domain.Annotation;
import com.linking.annotation.dto.*;
import com.linking.annotation.persistence.AnnotationMapper;
import com.linking.annotation.persistence.AnnotationRepository;
import com.linking.block.domain.Block;
import com.linking.block.persistence.BlockRepository;
import com.linking.global.exception.NoAuthorityException;
import com.linking.global.message.ErrorMessage;
import com.linking.group.controller.GroupEventHandler;
import com.linking.page.controller.PageEventHandler;
import com.linking.page.controller.PageSseHandler;
import com.linking.page.dto.PageIdRes;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnnotationService {
    private final PageSseHandler pageSseHandler;
    private final GroupEventHandler groupEventHandler;
    private final PageEventHandler pageEventHandler;
    private final AnnotationRepository annotationRepository;
    private final AnnotationMapper annotationMapper;
    private final PageCheckRepository pageCheckRepository;
    private final BlockRepository blockRepository;
    private final ParticipantRepository participantRepository;

    public AnnotationRes createAnnotation(AnnotationCreateReq req, Long userId) {
        Block block = blockRepository.findById(req.getBlockId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_BLOCK));

        Participant participant = participantRepository.findByUserAndProjectId(userId, req.getProjectId())
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
        // 페이지 들어가 있는 유저 아이디 목록
        Set<Long> enteringUserIds = pageSseHandler.enteringUserIds(block.getPage().getId());

        // 주석 개수 증가 이벤트
//        groupEventHandler.po
        groupEventHandler.postAnnotation(block.getPage().getGroup().getProject().getProjectId(), enteringUserIds, new PageIdRes(block.getPage().getId(), block.getPage().getGroup().getId()));
        // 주석 생성 이벤트
        pageEventHandler.postAnnotation(block.getPage().getId(), userId, annotationRes);

        return annotationRes;
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

        AnnotationUpdateRes annotationUpdateRes = AnnotationUpdateRes.builder()
                .annotationId(annotationRes.getAnnotationId())
                .blockId(annotationRes.getBlockId())
                .content(annotationRes.getContent())
                .lastModified(annotationRes.getLastModified())
                .build();

        // 주석 내용 수정 이벤트
        pageEventHandler.updateAnnotation(annotation.getBlock().getPage().getId(), userId, annotationUpdateRes);

        return annotationRes;
    }

    public void deleteAnnotation(Long annotationId, Long projectId, Long userId) {

        Annotation annotation = annotationRepository.findById(annotationId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_ANNOTATION));

        // 본인이 작성한 주석 아닌 경우 삭제 불가. 주석의 작성자가 탈퇴 또는 프로젝트 나간 경우 삭제 가능
        Participant writer = annotation.getParticipant();
        if (writer != null && writer.getUser().getUserId() != userId) {
            throw new NoAuthorityException("해당 주석을 삭제할 권한이 없습니다.");
        }

        Long groupId = annotation.getBlock().getPage().getGroup().getId();
        Long pageId = annotation.getBlock().getPage().getId();
        Long blockId = annotation.getBlock().getId();
        annotationRepository.delete(annotation);

        Participant participant = participantRepository.findByUserAndProjectId(userId, projectId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));

        // 해당 페이지, 참여자(주석생성한 팀원 제외)로 pageCheck 조회하여 annoNotCount 감소시킴
        List<PageCheck> pageCheckList = pageCheckRepository.findAllByPageId(pageId);
        pageCheckList.forEach(pc -> {
            if (pc.getParticipant().getParticipantId() != participant.getParticipantId()) {
                pc.reduceAnnoNotCount();
                pageCheckRepository.save(pc);
            }
        });

        // 페이지 들어가 있는 유저 아이디 목록
        Set<Long> enteringUserIds = pageSseHandler.enteringUserIds(pageId);
        // 주석 개수 감소 이벤트
        groupEventHandler.deleteAnnotation(projectId, enteringUserIds, new PageIdRes(pageId, groupId));
        // 주석 삭제 이벤트
        pageEventHandler.deleteAnnotation(pageId, userId, new AnnotationIdRes(annotationId, blockId));
    }
}
