package com.linking.document.dto;


import com.linking.group.dto.GroupRes;

import java.util.List;

public class DocumentEvent {
    private Long projectId;

    private List<GroupRes> groupResList;

    public DocumentEvent(Long projectId, List<GroupRes> groupResList) {
        this.projectId = projectId;
        this.groupResList = groupResList;
    }

    public Long getProjectId() {
        return projectId;
    }

    public List<GroupRes> getGroupResList() {
        return groupResList;
    }
}
