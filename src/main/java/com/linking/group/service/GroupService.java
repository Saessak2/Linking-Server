package com.linking.group.service;

import com.linking.document.domain.Document;
import com.linking.document.persistence.DocumentMapper;
import com.linking.document.persistence.DocumentRepository;
import com.linking.global.ErrorMessage;
import com.linking.group.domain.Group;
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
    private final DocumentMapper documentMapper;
    private final ProjectRepository projectRepository;
    private final DocumentRepository documentRepository;


    public GroupRes createGroup(GroupCreateReq groupCreateReq) throws Exception{
        Project refProject = projectRepository.getReferenceById(groupCreateReq.getProjectId());

        // TODO check duplicated docIndex
        Document document = documentMapper.toEntity(groupCreateReq);
        document.setProject(refProject);

        Group group = groupMapper.toEntity(groupCreateReq);
        group.setDocument(document);

        groupRepository.save(group);

        GroupRes groupRes = GroupRes.builder()
                .docId(document.getId())
                .title(document.getTitle())
                .projectId(document.getProject().getProjectId())
                .build();

        // TODO 양방향 연관관계 설정 ?
//        findProject.addDocument(group);
        return groupRes;
    }

    public GroupRes updateGroup(GroupUpdateReq groupUpdateReq) throws NoSuchElementException{

        documentRepository.updateTitle(groupUpdateReq.getDocId(), groupUpdateReq.getTitle());
        Document findDocument = documentRepository.findById(groupUpdateReq.getDocId())
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));

        GroupRes groupRes = GroupRes.builder()
                .docId(findDocument.getId())
                .title(findDocument.getTitle())
                .projectId(findDocument.getProject().getProjectId())
                .build();

        return groupRes;
    }

    public void deleteGroup(Long docId) throws NoSuchElementException{
        Document findDocument = documentRepository.findById(docId)
                .orElseThrow(() -> new NoSuchElementException(ErrorMessage.NO_GROUP));
        // 그룹 삭제 시 페이지 모두 삭제하는 경우
//        findGroup.removeAllPages();
        documentRepository.delete(findDocument);
        // TODO 그룹도 삭제되는지 확인

        // TODO 프로젝트의 document 리스트에서 삭제 되는지 확인 필요함
    }
}
