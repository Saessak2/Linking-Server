package com.linking.chat.persistence;

import com.linking.chatroom.domain.ChatRoom;
import com.linking.chat.domain.Chat;
import com.linking.chat.dto.ChatReq;
import com.linking.chat.dto.ChatRes;
import com.linking.participant.domain.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Component
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ChatMapper {

    List<ChatRes> toRes(List<Chat> chatList);

    default ChatRes toRes(Chat chat){
        if(chat == null)
            return null;

        ChatRes.ChatResBuilder chatResBuilder = ChatRes.builder();
        return chatResBuilder
                .userName(chat.getParticipant().getUserName())
                .content(chat.getContent())
                .sentDatetime(chat.getSentDatetime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a").withLocale(Locale.ENGLISH)))
                .build();
    }

    default Chat toMessage(ChatReq chatReq, Participant participant, ChatRoom chatRoom){
        if(chatReq == null || participant == null)
            return null;

        Chat.ChatBuilder chatBuilder = Chat.builder();
        return chatBuilder
                .participant(participant)
                .chatroom(chatRoom)
                .content(chatReq.getContent())
                .sentDatetime(LocalDateTime.parse(chatReq.getSentDatetime(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a").withLocale(Locale.ENGLISH)))
                .build();

    }

}
