package com.linking.pageCheck.service;

import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.persistence.PageCheckMapper;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageCheckService {
    private final PageCheckRepository pageCheckRepository;
    private final GroupRepository groupRepository;
    private final ParticipantRepository participantRepository;

    private final PageCheckMapper pageCheckMapper;

    public List<PageCheckRes> toPageCheckResList(List<PageCheck> pageCheckList, Long userId, Set<Long> enteringUserIds) {
        List<PageCheckRes> pageCheckResList = new ArrayList<>();

        if (pageCheckList.isEmpty())
            throw new RuntimeException("PageCheckList cannot be empty");

        pageCheckList.forEach(pageCheck -> {
            User user = pageCheck.getParticipant().getUser();

            if (user.getUserId() == userId) {  // 조회한 사용자 (userId)의
                pageCheck.updateLastChecked(); // 페이지 확인 시간 업뎃
                pageCheck.resetAnnoNotCount();  // 주석 알림 개수 0으로 리셋
                pageCheckRepository.save(pageCheck);
            }
            PageCheckRes pageCheckRes = pageCheckMapper.toDto(pageCheck, user.getFullName(), user.getUserId());
            if (enteringUserIds.contains(user.getUserId()))
                pageCheckRes.setIsEntering(true);
            else
                pageCheckRes.setIsEntering(false);

            pageCheckResList.add(pageCheckRes);
        });

        // pageCheck -> user fullName 순으로 정렬
        List<PageCheckRes> sortedPageCheckList = pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());

        return sortedPageCheckList;
    }


    // 페이지 나갈 때 마지막 열람 시간 업뎃
    public PageCheckRes updatePageLastChecked(Long pageId, Long projectId, Long userId) {
        // 팀원 조회
        Optional<Participant> participantOptional = participantRepository.findByUserAndProjectId(userId, projectId);
        if (participantOptional.isPresent()) {
            Participant participant = participantOptional.get();
            // 페이지 체크 조회
            Optional<PageCheck> pageCheckOptional = pageCheckRepository.findByPageAndPartId(pageId, participant.getParticipantId());
            if (pageCheckOptional.isPresent()) {
                PageCheck pageCheck = pageCheckOptional.get();
                pageCheck.updateLastChecked(); // 마지막 열람 시간 업뎃
                pageCheckRepository.save(pageCheck);
                // 페이지 체크 응답으로  변환
                PageCheckRes pageCheckRes = pageCheckMapper.toDto(pageCheck, participant.getUserName(), participant.getUser().getUserId());
                pageCheckRes.setIsEntering(true);
                return pageCheckRes;
            }
            return null;
        }
        return null;
    }

    // 팀원 추가시 페이지마다 해당 팀원의 페이지 체크 생성
    public void createPageCheckForAddParticipant(Participant participant) {
        List<Group> groups = groupRepository.findAllByProjectId(participant.getProject().getProjectId());
        for (Group group : groups) {
            for (Page page : group.getPageList()) {
                PageCheck pageCheck = new PageCheck(participant, page);
                pageCheckRepository.save(pageCheck);
            }
        }
    }
}
