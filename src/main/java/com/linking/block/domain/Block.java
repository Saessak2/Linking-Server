package com.linking.block.domain;

import com.linking.annotation.domain.Annotation;
import com.linking.page.domain.Page;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "block")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Block {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id")
    private Long id;
    
    // 필요여부에 따라 없앨 수 있음
    private int blockOrder;

    @Column(length = 100)
    private String title;

    @Column(columnDefinition = "TEXT")  // TEXT 타입은 65,535bytes
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id")
    private Page page;

    @OneToMany(mappedBy = "block", cascade = CascadeType.ALL)
    @OrderBy("id asc")
    private List<Annotation> annotationList;



    @Builder
    public Block(int blockOrder, Page page, List<Annotation> annotationList) {
        this.blockOrder = blockOrder;
        this.page = page;
        this.annotationList = annotationList;
    }

    @PrePersist
    public void prePersist() {
        this.title = this.title == null ? "untitled" : this.title;
        this.content = this.content == null ? "" : this.content;
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
