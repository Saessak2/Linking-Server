package com.linking.annotation;

import com.linking.block.Block;
import com.linking.user.User;
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

    /**
     * field
     */

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "annotation_id")
    private Long id;

    @Column(length = 255)
    private String content;

    @NotNull
    private LocalDateTime createdDatetime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_id")
    private Block block;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String userName;

    /**
     * constructor
     */


    /**
     * method
     */

    public void setBlock(Block block) {
        this.block = block;
        if (!block.getAnnotationList().contains(this)) {
            block.getAnnotationList().add(this);
        }
    }

    public void setUser(User User) {
        this.user = user;
    }
}
