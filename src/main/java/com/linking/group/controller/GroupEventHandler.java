package com.linking.group.controller;

import com.linking.group.dto.GroupIdRes;
import com.linking.group.dto.GroupRes;
import com.linking.page.dto.PageIdRes;
import com.linking.page.dto.PageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class GroupEventHandler {

    private final GroupSseHandler groupSseHandler;

    @Async("eventCallExecutor")
    public void postGroup(Long projectId, Long userId, GroupRes res) {
        groupSseHandler.send(projectId, userId, "postGroup", res);
    }

    @Async("eventCallExecutor")
    public void putGroupName(Long projectId, Long userId, GroupRes res) {
        groupSseHandler.send(projectId, userId, "putGroupName", res);
    }

    @Async("eventCallExecutor")
    public void deleteGroup(Long projectId, Long userId, GroupIdRes res) {
        groupSseHandler.send(projectId, userId, "deleteGroup", res);
    }

    @Async("eventCallExecutor")
    public void postPage(Long projectId, Long userId, PageRes res) {
        groupSseHandler.send(projectId, userId, "postPage", res);
    }

    @Async("eventCallExecutor")
    public void deletePage(Long projectId, Long userId, PageIdRes res) {
        groupSseHandler.send(projectId, userId, "deletePage", res);
    }

    @Async("eventCallExecutor")
    public void postAnnoNot(Long projectId, Set<Long> userIds, PageIdRes res) {
        groupSseHandler.send(projectId, userIds, "postAnnoNot", res);
//        groupSseHandler.send(projectId, userId, "postAnnotation", res);
    }

    @Async("eventCallExecutor")
    public void deleteAnnoNot(Long projectId, Set<Long> userIds, PageIdRes res) {
        groupSseHandler.send(projectId, userIds, "deleteAnnoNot", res);
    }
}
