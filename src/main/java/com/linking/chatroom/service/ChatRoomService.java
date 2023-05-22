package com.linking.chatroom.service;

import com.linking.chatroom.dto.ChatRoomRes;
import com.linking.chatroom.repository.ChatRoomMapper;
import com.linking.chatroom.repository.ChatRoomRepository;
import com.linking.project.domain.Project;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMapper chatRoomMapper;

    public ChatRoomRes getChatRoomById(Long id){
        return chatRoomRepository.findChatRoomByProject(new Project(id))
                .map(chatRoomMapper::toRes)
                .orElseThrow(NoSuchElementException::new);
    }

}
