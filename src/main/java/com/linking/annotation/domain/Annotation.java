package com.linking.annotation.domain;

import com.linking.block.domain.Block;
import com.linking.participant.domain.Participant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "annotation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Annotation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annotation_id")
    private Long id;

    private LocalDateTime createdDatetime;
    private LocalDateTime lastModified;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private String userName;

    @Builder
    public Annotation(LocalDateTime lastModified, String content, Block block, Participant participant, String userName) {
        this.lastModified = lastModified;
        this.content = content;
        this.block = block;
        this.participant = participant;
        this.userName = userName;
    }

    @PrePersist
    public void prePersist() {
        this.createdDatetime = LocalDateTime.now();
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
