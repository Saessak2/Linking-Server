package com.linking.annotation.domain;

import com.linking.block.domain.Block;
import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "annotation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Annotation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annotation_id")
    private Long id;

    @Column(length = 255)
    private String content;

    private LocalDateTime lastModified;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private String userName;


    @Builder
    public Annotation(String content, LocalDateTime lastModified, Block block, Participant participant, String userName) {
        this.content = content;
        this.lastModified = lastModified;
        this.block = block;
        this.participant = participant;
        this.userName = userName;
    }

    public void setBlock(Block block) {
        this.block = block;
        if (!block.getAnnotationList().contains(this)) {
            block.getAnnotationList().add(this);
        }
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public void updateContent(String content) {
        this.content = content;
        this.lastModified = LocalDateTime.now();
    }

}
