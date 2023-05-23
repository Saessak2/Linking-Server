package com.linking.chatroom.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final ChatService chatService;

    @GetMapping("/{id}/messages")
    public ResponseEntity<Object> getMessages(@PathVariable Long id, Pageable pageable){
        return ResponseHandler.generateOkResponse(chatService.getRecentChatList(id, pageable));
    }

}
