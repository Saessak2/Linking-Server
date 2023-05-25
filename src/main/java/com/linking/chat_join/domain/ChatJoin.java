package com.linking.chat_join.domain;

import com.linking.chatroom.domain.ChatRoom;
import com.linking.participant.domain.Participant;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chat_join")
public class ChatJoin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_join_id")
    private Long chatJoinId;

    @ManyToOne
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private Participant participant;

}
