package com.linking.chatroom.repository;

import com.linking.chatroom.dto.ChatRoomRes;
import com.linking.chatroom.domain.ChatRoom;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

@Component
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatRoomMapper {

    ChatRoomRes toRes(ChatRoom chatRoom);
    ChatRoom toEntity(ChatRoomRes chatRoomRes);

}
