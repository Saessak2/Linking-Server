package com.linking.group.controller;

import com.linking.group.dto.GroupIdRes;
import com.linking.group.dto.GroupRes;
import com.linking.page.dto.PageIdRes;
import com.linking.page.dto.PageRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GroupEventHandler {

    private final GroupSseHandler groupSseHandler;

    public void postGroup(Long projectId, Long userId, GroupRes res) {
        groupSseHandler.send(projectId, userId, "postGroup", res);
    }

    public void putGroupName(Long projectId, Long userId, GroupRes res) {
        groupSseHandler.send(projectId, userId, "putGroupName", res);
    }

    public void deleteGroup(Long projectId, Long userId, GroupIdRes res) {
        groupSseHandler.send(projectId, userId, "deleteGroup", res);
    }

    public void postPage(Long projectId, Long userId, PageRes res) {
        groupSseHandler.send(projectId, userId, "postPage", res);
    }

    public void deletePage(Long projectId, Long userId, PageIdRes res) {
        groupSseHandler.send(projectId, userId, "deletePage", res);
    }

    public void postAnnotation(Long projectId, Long userId, PageIdRes res) {
//        groupSseHandler.send(projectId, );
        groupSseHandler.send(projectId, userId, "postAnnotation", res);
    }

    public void deleteAnnotation(Long projectId, Long userId, PageIdRes res) {
        groupSseHandler.send(projectId, userId, "deleteAnnotation", res);
    }
}
