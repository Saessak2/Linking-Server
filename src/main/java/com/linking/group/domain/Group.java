package com.linking.group.domain;

import com.linking.document.domain.Document;
import com.linking.project.domain.Project;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"group\"")
@DiscriminatorValue("G")
@PrimaryKeyJoinColumn(name = "group_id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group extends Document {

    @Column(length = 10)
    private String name;


    @Builder
    protected Group(int docIndex, Project project, List<Document> childList,  String name) {
        super(docIndex, project, childList);
        this.name = name;
    }

    @PrePersist
    public void prePersist() {
        this.name = this.name == null ? "New Group" : this.name;
    }

    public void updateName(String name) {
        this.name = name;
    }

}

