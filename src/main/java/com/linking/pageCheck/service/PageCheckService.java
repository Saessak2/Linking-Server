package com.linking.pageCheck.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.persistence.PageRepository;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.persistence.PageCheckMapper;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PageCheckService {
    private final PageCheckRepository pageCheckRepository;
    private final GroupRepository groupRepository;
    private final PageRepository pageRepository;

    private final PageCheckMapper pageCheckMapper;

    public List<PageCheckRes> getPageCheckList(Page page, Long userId) throws NoSuchElementException{
        List<PageCheckRes> pageCheckResList = new ArrayList<>();

        List<PageCheck> pageCheckList = page.getPageCheckList();
        if (pageCheckList.isEmpty())
            throw new RuntimeException("cannot pageCheckList is empty");


        pageCheckList.forEach(pageCheck -> {
            User user = pageCheck.getParticipant().getUser();

            if (user.getUserId() == userId) {  // 조회한 사용자의 페이지 확인 시간 업뎃
                pageCheck.updateLastChecked();
                pageCheckRepository.save(pageCheck);
            }
            pageCheckResList.add(pageCheckMapper.toDto(pageCheck, user.getFullName(), user.getUserId()));
        });

        return pageCheckResList;
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
