package com.linking.participant.domain;

import com.linking.project.domain.Project;
import com.linking.user.domain.User;
import lombok.*;

import javax.persistence.*;
import java.io.PushbackReader;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "participant")
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long participantId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void setUser(User user) {
        this.user = user;
    }

    public void setProject(Project project) {
        this.project = project;
        if (!project.getParticipantList().contains(this)) {
            project.getParticipantList().add(this);
        }
    }
}
