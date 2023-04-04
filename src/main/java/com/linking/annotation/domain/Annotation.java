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
@AllArgsConstructor
@Builder
public class Annotation {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annotation_id")
    private Long id;

    @Column(length = 255)
    private String content;

    private LocalDateTime createdDatetime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    private String userName;

    public void setBlock(Block block) {
        this.block = block;
        if (!block.getAnnotationList().contains(this)) {
            block.getAnnotationList().add(this);
        }
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }
}
