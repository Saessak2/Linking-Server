package com.linking.chatroom.domain;

import com.linking.message.domain.Message;
import com.linking.message.dto.MessageReq;
import com.linking.message.service.MessageService;
import com.linking.project.domain.Project;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "chatroom")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long chatRoomId;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Builder
    public ChatRoom(Long chatRoomId, Project project) {
        this.chatRoomId = chatRoomId;
        this.project = project;
    }

    @Transient
    private Set<WebSocketSession> sessions = new HashSet<>();

//    @Transient
//    public void handleAction(WebSocketSession session, MessageService messageService, MessageReq messageReq){
//
//    }

    @Transient
    public void handleEntering(WebSocketSession session) {
        sessions.add(session);
    }

    @Transient
    public <T> void handleMessage(MessageService messageService, MessageReq messageReq) {
        Message message = messageService.saveMessage(this, messageReq);
        sessions.parallelStream()
                .forEach(session -> messageService.sendMessages(session, message));
    }

}
