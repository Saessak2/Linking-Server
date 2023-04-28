package com.linking.pageCheck.service;

import com.linking.global.message.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.controller.PageEventHandler;
import com.linking.page.domain.Page;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckUpdateRes;
import com.linking.pageCheck.persistence.PageCheckMapper;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class PageCheckService {
    private final PageEventHandler pageEventHandler;
    private final PageCheckRepository pageCheckRepository;
    private final PageCheckMapper pageCheckMapper;
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;

    public void updatePageChecked(Long pageId, Long projectId, Long userId, String event) {

        // 팀원 조회
        Participant participant = participantRepository.findByUserAndProjectId(userId, projectId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));
        // 페이지 체크 조회
        PageCheck pageCheck = pageCheckRepository.findByPageAndPartId(pageId, participant.getParticipantId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PAGE_CHECK));

        pageCheck.updateLastChecked(); // 마지막 열람 시간 업뎃
        if (event.equals("enter"))
            pageCheck.resetAnnoNotCount(); // 주석 알림 개수 리셋

        PageCheckUpdateRes pageCheckUpdateRes = pageCheckMapper.toPageCheckUpdateDto(pageCheckRepository.save(pageCheck));

        if (event.equals("leave"))
            pageEventHandler.leave(pageId, userId, pageCheckUpdateRes); // leave 이벤트 발행
        else if (event.equals("enter"))
            pageEventHandler.enter(pageId, userId, pageCheckUpdateRes); // enter 이벤트 발행
    }

    // 팀원 추가시 페이지마다 해당 팀원의 페이지 체크 생성
    public void createPageCheck(Participant participant) {
        List<Group> groups = groupRepository.findAllByProjectId(participant.getProject().getProjectId());
        for (Group group : groups) {
            for (Page page : group.getPageList()) {
                PageCheck pageCheck = new PageCheck(participant, page);
                pageCheckRepository.save(pageCheck);
            }
        }
    }
}
