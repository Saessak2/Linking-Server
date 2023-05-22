package com.linking.chatroom.controller;

import com.linking.global.common.ResponseHandler;
import com.linking.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// TODO: to be deleted
@RestController
@RequiredArgsConstructor
@RequestMapping("/chatroom")
public class ChatRoomController {

    private final MessageService messageService;

    @GetMapping("/project/{id}")
    public ResponseEntity<Object> getMessages(@PathVariable Long id){
        return ResponseHandler.generateOkResponse(messageService.getMessagesByProjectId(id));
    }

}
