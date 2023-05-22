package com.linking.message.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.message.domain.Message;
import com.linking.message.dto.MessageReq;
import com.linking.message.dto.MessageRes;
import com.linking.message.persistence.MessageMapper;
import com.linking.message.persistence.MessageRepository;
import com.linking.participant.persistence.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageMapper messageMapper;

    private final ParticipantRepository participantRepository;

    private final ObjectMapper objectMapper;

    public void sendMessages(WebSocketSession webSocketSession, Message message){
        try{
            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(messageMapper.toRes(message))));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public List<MessageRes> getRecentMessages(Long id, Pageable pageable){
        Page<Message> messagePage = messageRepository.findMessagesByChatroom(new ChatRoom(id), pageable);
        if(messagePage!=null && messagePage.hasContent())
            return messageMapper.toRes(messagePage.getContent());
        return new ArrayList<>();
    }

    public Message saveMessage(ChatRoom chatRoom, MessageReq messageReq){
        return messageRepository.save(
                participantRepository.findByUserAndProjectId(messageReq.getUserId(), messageReq.getProjectId())
                        .map(p -> messageMapper.toMessage(messageReq, p, chatRoom)).orElseThrow(NoSuchElementException::new));
    }

}
