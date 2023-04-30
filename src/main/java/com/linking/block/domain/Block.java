package com.linking.block.domain;

import com.linking.annotation.domain.Annotation;
import com.linking.global.common.BaseTimeEntity;
import com.linking.page.domain.Page;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "block")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // "@CreatedDate"를 사용하기 위한 Annotation
public class Block extends BaseTimeEntity implements Persistable<Long>{

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

    @OneToMany(mappedBy = "block", cascade = CascadeType.REMOVE)
    @OrderBy("createdDatetime asc")
    private List<Annotation> annotationList;

    @Builder
    public Block(int blockOrder, String title) {
        this.blockOrder = blockOrder;
        this.title = title;
    }

    @PrePersist
    public void prePersist() {
        this.title = this.title == null ? "untitled" : this.title;
        this.content = this.content == null ? "" : this.content;
    }

    public void setPage(Page page) {
        if (this.page != null)
            this.page.getBlockList().remove(this);
        this.page = page;
        if (!page.getBlockList().contains(this)) {
            page.getBlockList().add(this);
        }
    }

    public void updateOrder(int order) {
        this.blockOrder = order;
    }

    @Override
    public boolean isNew() {  // createdDate == null 이면 새로운 데이터로 취급함.
        return super.getCreatedDate() == null;
    }
}
