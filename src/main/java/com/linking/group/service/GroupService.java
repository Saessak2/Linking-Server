package com.linking.group.service;

import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.group.dto.GroupUpdateTitleReq;
import com.linking.group.persistence.GroupMapper;
import com.linking.group.persistence.GroupRepository;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMapper groupMapper;
    private final ProjectRepository projectRepository;


    // TODO check duplicated docIndex
    public GroupRes createGroup(GroupCreateReq groupCreateReq) throws Exception{
        Project refProject = projectRepository.getReferenceById(groupCreateReq.getProjectId());

        Group group = groupMapper.toEntity(groupCreateReq);
        group.setProject(refProject);

        return groupMapper.toDto(groupRepository.save(group));
    }

    public GroupRes updateGroup(GroupUpdateTitleReq req) throws NoSuchElementException{
        Group findGroup = groupRepository.findById(req.getGroupId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        if (!findGroup.getName().equals(req.getName())) {
            findGroup.updateName(req.getName());
            return groupMapper.toDto(groupRepository.save(findGroup));
        }


//        } else if (findGroup.getGroupOrder() != req.getOrder()) { // 그룹 순서 변경
//
//            List<Group> groups = groupRepository.findAllByProject(findGroup.getProject().getProjectId());
//            groups.removeIf(g -> g.getId().equals(findGroup.getId()));
//            groups.add(req.getOrder(), findGroup);
//
//            int order = 0;
//            for (Group group : groups) {
//                group.updateOrder(order);
//                groupRepository.save(group);
//            }
//            findGroup.updateOrder(req.getOrder()); //TODO 이렇게 해도 되나...?
//            return groupMapper.toDto(findGroup);
//        }
        return groupMapper.toDto(findGroup);
    }

    public void deleteGroup(Long docId) throws NoSuchElementException{

        // TODO 그룹도 삭제되는지 확인

        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함
    }
}
