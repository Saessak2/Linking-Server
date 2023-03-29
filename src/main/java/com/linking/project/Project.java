package com.linking.project;

import com.linking.document.Document;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "project")

@Builder
@AllArgsConstructor
public class Project {

    /**
     * field
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;



    @OneToMany(mappedBy = "project", cascade = CascadeType.REMOVE)
    private List<Document> documentList;

    public void addDocument(Document document) {
        this.documentList.add(document);
        if (document.getProject() != this) {
            document.setProject(this);
        }
    }
}
