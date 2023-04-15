package com.linking.group.event;

import com.linking.group.dto.GroupRes;
import lombok.Getter;

@Getter
public class GroupEvent {

    private int resType;
    private int publishType;
    private Long userId;
    private Long projectId;
    private GroupRes groupRes;

    public GroupEvent(int resType, int publishType, Long userId, Long projectId, GroupRes groupRes) {
        this.resType = resType;
        this.publishType = publishType;
        this.userId = userId;
        this.projectId = projectId;
        this.groupRes = groupRes;
    }
}
