package com.linking.group.dto;


import java.util.List;

public class DocumentEvent {
    private Long projectId;

    private List<GroupDetailedRes> groupDetailedResList;

    public DocumentEvent(Long projectId, List<GroupDetailedRes> groupDetailedResList) {
        this.projectId = projectId;
        this.groupDetailedResList = groupDetailedResList;
    }

    public Long getProjectId() {
        return projectId;
    }

    public List<GroupDetailedRes> getGroupResList() {
        return groupDetailedResList;
    }
}
