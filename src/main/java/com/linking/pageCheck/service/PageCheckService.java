package com.linking.pageCheck.service;

import com.linking.global.ErrorMessage;
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

@Service
@RequiredArgsConstructor
public class PageCheckService {
    private final PageCheckRepository pageCheckRepository;

    private final PageCheckMapper pageCheckMapper;
    private final ParticipantRepository participantRepository;

    public List<PageCheckRes> getPageCheckResList(Page page, Long userId) throws NoSuchElementException{
        List<PageCheck> pageCheckList = page.getPageCheckList();

        List<PageCheckRes> pageCheckResList = new ArrayList<>();
        if (pageCheckList.size() == 0) {
            pageCheckResList.add(pageCheckMapper.toEmptyDto());
        }

        for (PageCheck pageCheck : pageCheckList) {
            Long participantId = pageCheck.getParticipant().getParticipantId();
            Participant participant = participantRepository.findById(participantId)
                    .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_PARTICIPANT));

            User user = participant.getUser();
            if (user.getUserId() == userId) { // 페이지를 조회한 사용자 -> 확인 시간 업뎃
                pageCheck.updateLastChecked();
                pageCheckRepository.save(pageCheck);
            }

            pageCheckResList.add(pageCheckMapper.toDto(pageCheck, user.getFullName(), user.getUserId()));
        }
        return pageCheckResList;
    }
}
