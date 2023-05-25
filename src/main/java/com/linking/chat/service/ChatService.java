package com.linking.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.linking.chatroom.domain.ChatRoom;
import com.linking.chat.domain.Chat;
import com.linking.chat.dto.ChatReq;
import com.linking.chat.dto.ChatRes;
import com.linking.chat.persistence.ChatMapper;
import com.linking.chat.persistence.ChatRepository;
import com.linking.chatroom.repository.ChatRoomRepository;
import com.linking.participant.persistence.ParticipantRepository;
import com.linking.project.domain.Project;
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
public class ChatService {

    private final ObjectMapper objectMapper;

    private final ChatRepository chatRepository;
    private final ChatMapper chatMapper;
    private final ChatRoomRepository chatRoomRepository;

    private final ParticipantRepository participantRepository;

//    public void sendChat(WebSocketSession webSocketSession, Chat chat){
//        try{
//            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMapper.toRes(chat))));
//        } catch (IOException e) {
//            log.error(e.getMessage(), e);
//        }
//    }

    public List<ChatRes> getRecentChatList(Long id, Pageable pageable){
        log.info("page info {} -------------------------------------------------------------------" ,pageable);
        ChatRoom chatRoom = chatRoomRepository.findChatRoomByProject(new Project(id)).orElseThrow(NoSuchElementException::new);
        Page<Chat> messagePage = chatRepository.findMessagesByChatroom(chatRoom, pageable);
        if(messagePage != null && messagePage.hasContent())
            return chatMapper.toRes(messagePage.getContent());
        return new ArrayList<>();
    }

    public ChatRes saveChat(ChatRoom chatRoom, ChatReq chatReq) {
        return chatMapper.toRes(chatRepository.save(
                participantRepository.findByUserAndProjectId(chatReq.getUserId(), chatReq.getProjectId())
                        .map(p -> chatMapper.toMessage(chatReq, p, chatRoom)).orElseThrow(NoSuchElementException::new)));
    }

}
