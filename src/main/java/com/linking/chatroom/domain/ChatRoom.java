package com.linking.chatroom.domain;

import com.linking.project.domain.Project;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatroom")
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long chatRoomId;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    public ChatRoom(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

}
