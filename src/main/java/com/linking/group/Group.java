package com.linking.group;

import com.linking.document.Document;
import com.linking.project.Project;
import lombok.*;
import javax.persistence.*;

@Entity
@DiscriminatorValue("G")
@PrimaryKeyJoinColumn(name = "group_id")
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends Document {

    @Column(length = 10)
    private String name;

    /**
     * constructor
     */

    @Builder
    public Group(int doc_depth, int doc_index, Project project, String name) {
        super(doc_depth, doc_index, project);
        this.name = name;
    }



    /**
     * method
     */

    @PrePersist
    public void prePersist() {
        this.name = this.name == null ? "untitled" : this.name;
    }



}

