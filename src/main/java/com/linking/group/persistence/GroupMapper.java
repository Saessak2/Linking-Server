package com.linking.group.persistence;

import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GroupMapper {


    default GroupRes toDto(Group source) {
        if (source == null) {
            return null;
        }
        GroupRes.GroupResBuilder builder = GroupRes.builder();
        builder
                .groupId(source.getId())
                .projectId(source.getProject().getProjectId())
                .order(source.getGroupOrder())
                .name(source.getName());
        return builder.build();
    }


    default Group toEntity(GroupCreateReq source) {
        if (source == null) {
            return null;
        }
        Group.GroupBuilder builder = Group.builder();
        builder
                .name(source.getName())
                .idx(source.getOrder())
                .pageList(new ArrayList<>());

        return builder.build();
    }
}
