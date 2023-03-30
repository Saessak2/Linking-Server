package com.linking.block;

import com.linking.annotation.Annotation;
import com.linking.page.Page;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "block")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block {

    /**
     * field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;
    
    @NotNull
    private int index;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")  //TODO TEXT 타입은 65,535bytes
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @OneToMany(mappedBy = "block", cascade = CascadeType.REMOVE)
    private List<Annotation> annotationList;


    /**
     * constructor
     */

    public Block(int index, String title, String content, Page page, List<Annotation> annotationList) {
        this.index = index;
        this.title = title;
        this.content = content;
        this.page = page;
        this.annotationList = annotationList;
    }

    /**
     * method
     */

    @PrePersist
    public void prePersist() {
        this.title = this.title == null ? "untitled" : this.title;
    }

    public void setPage(Page page) {
        this.page = page;
        if (page.getBlockList().contains(this)) {
            page.getBlockList().add(this);
        }
    }

    public void addAnnotation(Annotation annotation) {
        this.annotationList.add(annotation);
        if(annotation.getBlock() != this) {
            annotation.setBlock(this);
        }
    }
}
