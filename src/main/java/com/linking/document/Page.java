package com.linking.document;

import com.linking.document.Group;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("P")
@PrimaryKeyJoinColumn(name = "page_id")
@Table(name = "page")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends Document{

    @NotNull
    private LocalDateTime createdDatetime;
    @NotNull
    private LocalDateTime updatedDatetime;

//    @ManyToOne
//    @JoinColumn(name = "id")
//    private Group parent;

//
//    @Builder
//    public Page(String name, int doc_depth, int doc_index, LocalDateTime createdDatetime, LocalDateTime updatedDatetime) {
//        super(name, doc_depth, doc_index);
//        this.createdDatetime = createdDatetime;
//        this.updatedDatetime = updatedDatetime;
//    }

    @Builder
    public Page(String name, int doc_depth, int doc_index, Document parent, LocalDateTime createdDatetime, LocalDateTime updatedDatetime) {
        super(name, doc_depth, doc_index, parent);
        this.createdDatetime = createdDatetime;
        this.updatedDatetime = updatedDatetime;
    }


}
