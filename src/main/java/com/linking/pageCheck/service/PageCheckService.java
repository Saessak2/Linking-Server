package com.linking.pageCheck.service;

import com.linking.group.domain.Group;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.pageCheck.dto.PageCheckRes;
import com.linking.pageCheck.persistence.PageCheckMapper;
import com.linking.pageCheck.persistence.PageCheckRepository;
import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PageCheckService {
    private final PageCheckRepository pageCheckRepository;
    private final GroupRepository groupRepository;

    private final PageCheckMapper pageCheckMapper;

    public List<PageCheckRes> toPageCheckResList(List<PageCheck> pageCheckList, Long userId) {
        List<PageCheckRes> pageCheckResList = new ArrayList<>();

        if (pageCheckList.isEmpty())
            throw new RuntimeException("PageCheckList cannot be empty");

        pageCheckList.forEach(pageCheck -> {
            User user = pageCheck.getParticipant().getUser();

            if (user.getUserId() == userId) {  // 조회한 사용자의 페이지 확인 시간 업뎃
                pageCheck.updateLastChecked();
                pageCheckRepository.save(pageCheck);
            }
            pageCheckResList.add(pageCheckMapper.toDto(pageCheck, user.getFullName(), user.getUserId()));
        });

        // pageCheck -> user fullName 순으로 정렬
        List<PageCheckRes> sortedPageCheckList = pageCheckResList.stream()
                .sorted(Comparator.comparing(PageCheckRes::getUserName))
                .collect(Collectors.toList());

        return sortedPageCheckList;
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
