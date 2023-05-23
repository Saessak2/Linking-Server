package com.linking.domain.group.persistence;

import com.linking.domain.group.domain.Group;
import com.linking.domain.group.dto.GroupCreateReq;
import com.linking.domain.group.dto.GroupDetailedRes;
import com.linking.domain.group.dto.GroupRes;
import com.linking.domain.page.dto.PageRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GroupMapper {

    default GroupRes toDto(Group source) {

        GroupRes builder = GroupRes.builder()
                .projectId(source.getProject().getProjectId())
                .groupId(source.getId())
                .name(source.getName())
                .pageResList(new ArrayList<>())
                .build();

        return builder;
    }

    default GroupDetailedRes toDto(Group source, List<PageRes> pageResList) {

        GroupDetailedRes builder = GroupDetailedRes.builder()
                .groupId(source.getId())
                .projectId(source.getProject().getProjectId())
                .name(source.getName())
                .pageResList(pageResList)
                .build();

        return builder;
    }


    default Group toEntity(GroupCreateReq source) {

        Group.GroupBuilder builder = Group.builder();
        builder
                .name(source.getName())
                .groupOrder(source.getOrder())
                .pageList(new ArrayList<>());

        return builder.build();
    }
}
