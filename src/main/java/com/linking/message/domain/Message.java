package com.linking.message.domain;

import com.linking.chatroom.domain.Chatroom;
import com.linking.participant.domain.Participant;
import com.linking.project.domain.Project;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "message")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private Chatroom chatroom;

    @Column(nullable = false)
    private String content;

    @Column(name = "sent_datetime", nullable = false)
    private LocalDateTime sentDatetime;

}
