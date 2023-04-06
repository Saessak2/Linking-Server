package com.linking.annotation.domain;

import com.linking.block.domain.Block;
import com.linking.participant.domain.Participant;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Table(name = "annotation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Annotation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annotation_id")
    private Long id;

    private LocalDate lastModified;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private String userName;

    @Builder
    public Annotation(LocalDate lastModified, String content, Block block, Participant participant, String userName) {
        this.lastModified = lastModified;
        this.content = content;
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
        this.lastModified = LocalDate.now();
    }
}
