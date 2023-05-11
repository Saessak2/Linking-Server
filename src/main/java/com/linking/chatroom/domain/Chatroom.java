package com.linking.chatroom.domain;

import com.linking.project.domain.Project;
import lombok.*;

import javax.persistence.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatroom")
public class Chatroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatroom_id")
    private Long chatroomId;

    @OneToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

}
