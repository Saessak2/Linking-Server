package com.linking.group.persistence;

import com.linking.group.domain.Group;
import com.linking.group.dto.GroupCreateReq;
import com.linking.group.dto.GroupRes;
import com.linking.page.dto.PageRes;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.ArrayList;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface GroupMapper {


    default GroupRes toDto(Group source, List<PageRes> pageResList) {

        GroupRes.GroupResBuilder builder = GroupRes.builder();
        builder
                .groupId(source.getId())
                .projectId(source.getProject().getProjectId())
                .name(source.getName())
                .order(source.getGroupOrder())
                .pageResList(pageResList);

        return builder.build();
    }


    default Group toEntity(GroupCreateReq source) {
        if (source == null) {
            return null;
        }
        Group.GroupBuilder builder = Group.builder();
        builder
                .name(source.getName())
                .groupOrder(source.getOrder())
                .pageList(new ArrayList<>());

        return builder.build();
    }
}
