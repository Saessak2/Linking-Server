package com.linking.chatroom.domain;

import com.linking.project.domain.Project;
import lombok.*;

import javax.persistence.*;

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

    public ChatRoom(Long chatRoomId) {
        this.chatRoomId = chatRoomId;
    }

}
