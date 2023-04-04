package com.linking.document.domain;

import com.linking.project.domain.Project;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "document")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Document {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;

    // 필요에 따라 없앨 수 있음.
    private int docIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("docIndex asc")
    private List<Document> childList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_doc_id")
    private Document parent;


    // 그룹 생성자
    protected Document(int docIndex, Project project, List<Document> childList) {
        this.docIndex = docIndex;
        this.project = project;
        this.childList = childList;
    }

    // 페이지 생성자
    protected Document(int docIndex, Project project, Document parent) {
        this.docIndex = docIndex;
        this.project = project;
        this.parent = parent;
    }

    public void setProject(Project project) {
        this.project = project;
        if (!project.getDocumentList().contains(this)) {
            project.getDocumentList().add(this);
        }
    }

    public void setParent(Document parent) {
        this.parent = parent;
        if (!parent.getChildList().contains(this)) {
            parent.getChildList().add(this);
        }
    }

    public void addChild(Document child) {
        this.childList.add(child);
        if (child.getParent() != this) {
            child.setParent(this);
        }
    }

    public void setDocIndex(int docIndex) {
        this.docIndex = docIndex;
    }
}
