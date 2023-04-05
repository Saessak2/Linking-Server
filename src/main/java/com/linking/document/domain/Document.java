package com.linking.document.domain;

import com.linking.project.domain.Project;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "document")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Document {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "document_id")
    private Long id;
    private int docIndex;
    private String title;

    @Enumerated(EnumType.STRING)
    private DType dType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("docIndex asc")
    private List<Document> childList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_doc_id")
    private Document parent;


    @Builder  // 그룹 생성자
    protected Document(int docIndex, Project project, List<Document> childList, String title, DType dType) {
        this.docIndex = docIndex;
        this.project = project;
        this.childList = childList;
        this.title = title;
        this.dType = dType;
    }

    @Builder // 페이지 생성자
    protected Document(int docIndex, Project project, Document parent, String title) {
        this.docIndex = docIndex;
        this.project = project;
        this.parent = parent;
        this.title = title;
        this.dType = dType;
    }


    @PrePersist
    public void prePersist() {
        this.title = this.title == null ? "untitled" : this.title;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public void removeDocument(Document document) {
        this.childList.remove(document);
        document.setParent(null);
    }

    public void removeAllPages() {
        List<Document> documents = this.getChildList();

        if (documents != null) {
            for (Document document : documents) {
                document.setParent(null);
            }
        }
    }

}
