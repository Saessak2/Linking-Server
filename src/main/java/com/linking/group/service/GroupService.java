package com.linking.group.service;

import com.linking.document.domain.Document;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateReq;
import com.linking.group.persistence.GroupRepository;
import com.linking.page.dto.PageRes;
import com.linking.project.ProjectRepository;
import com.linking.project.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final ProjectRepository projectRepository;


    public GroupRes createGroup(GroupCreateReq groupCreateReq) throws NoSuchElementException{

        Project findProject = projectRepository.findById(groupCreateReq.getProjectId())
                .orElseThrow(() -> new NoSuchElementException());

        // TODO docIndex 중복 체크

        Group group = Group.builder()
                .name(groupCreateReq.getName())
                .docIndex(groupCreateReq.getDocIndex())
                .childList(new ArrayList<>())
                .project(findProject)
                .build();

        // TODO 양방향 연관관계 설정
//        findProject.addDocument(group);
        groupRepository.save(group);

        Group saveGroup = groupRepository.save(group);
        GroupRes groupRes = GroupRes.builder()
                .groupId(saveGroup.getId())
                .projectId(saveGroup.getProject().getProjectId())
                .name(saveGroup.getName())
                .build();

        return groupRes;

    }

    public GroupRes updateGroup(GroupUpdateReq groupUpdateReq) throws NoSuchElementException{

        Group findGroup = groupRepository.findById(groupUpdateReq.getGroupId())
                .orElseThrow(() -> new NoSuchElementException());

        // 이름 변경
        findGroup.updateName(groupUpdateReq.getName());
        Group saveGroup = groupRepository.save(findGroup);


        GroupRes groupRes = GroupRes.builder()
                .groupId(saveGroup.getId())
                .projectId(saveGroup.getProject().getProjectId())
                .name(saveGroup.getName())
                .build();

        return groupRes;
    }

    public void deleteGroup(Long groupId) throws NoSuchElementException{

        groupRepository.delete(
                groupRepository.findById(groupId).orElseThrow(() -> new NoSuchElementException())
        );
        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함
    }

    public GroupRes findGroup(Long groupId) {
        Group findGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new NoSuchElementException());

        List<Document> childList = findGroup.getChildList();
        List<PageRes> pageResList = new ArrayList<>();
        for (Document child: childList) {
            PageRes pageRes = PageRes.builder()
                    .
                    .build();
        }
    }
}
