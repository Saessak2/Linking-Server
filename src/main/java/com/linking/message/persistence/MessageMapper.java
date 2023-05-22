package com.linking.message.persistence;

import com.linking.chatroom.domain.ChatRoom;
import com.linking.message.domain.Message;
import com.linking.message.dto.MessageReq;
import com.linking.message.dto.MessageRes;
import com.linking.participant.domain.Participant;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Component
@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MessageMapper {

    MessageRes toRes(Message message);

    default Message toMessage(MessageReq messageReq, Participant participant, ChatRoom chatRoom){
        if(messageReq == null || participant == null)
            return null;

        Message.MessageBuilder messageBuilder = Message.builder();
        return messageBuilder
                .participant(participant)
                .chatroom(chatRoom)
                .content(messageReq.getContent())
                .sentDatetime(LocalDateTime.parse(messageReq.getSentDatetime(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm a").withLocale(Locale.ENGLISH)))
                .build();

    }

}
