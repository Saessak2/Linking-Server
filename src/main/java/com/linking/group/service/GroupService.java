package com.linking.group.service;

import com.linking.global.ErrorMessage;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateReq;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;


    public GroupRes createGroup(GroupCreateReq groupCreateReq) throws Exception{
        Project refProject = projectRepository.getReferenceById(groupCreateReq.getProjectId());

        // TODO check duplicated docIndex

        // TODO 양방향 연관관계 설정 ?
//        findProject.addDocument(group);
        return groupRes;
    }

    public GroupRes updateGroup(GroupUpdateReq groupUpdateReq) throws NoSuchElementException{

    }

    public void deleteGroup(Long docId) throws NoSuchElementException{

        // TODO 그룹도 삭제되는지 확인

        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함
    }
}
