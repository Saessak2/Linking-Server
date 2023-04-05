package com.linking.group.service;

import com.linking.document.domain.Document;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateReq;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.domain.Page;
import com.linking.page.dto.PageRes;
import com.linking.page.persistence.PageMapper;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final PageMapper pageMapper;
    private final ProjectRepository projectRepository;


    public GroupRes createGroup(GroupCreateReq groupCreateReq) throws Exception{
        Project refProject = projectRepository.getReferenceById(groupCreateReq.getProjectId());

        // TODO docIndex 중복 체크
        Group group = groupMapper.toEntity(groupCreateReq);
        group.setProject(refProject);

        // TODO 양방향 연관관계 설정 ?
//        findProject.addDocument(group);

        return groupMapper.toDto(groupRepository.save(group));
    }

    public GroupRes updateGroup(GroupUpdateReq groupUpdateReq) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(groupUpdateReq.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        // 이름 변경
        findGroup.updateName(groupUpdateReq.getName());

        return groupMapper.toDto(groupRepository.save(findGroup));
    }

    public void deleteGroup(Long groupId) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
        // 그룹 삭제 시 페이지 모두 삭제하는 경우
//        findGroup.removeAllPages();
        groupRepository.delete(findGroup);
        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함
    }
}
