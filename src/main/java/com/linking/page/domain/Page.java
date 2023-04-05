package com.linking.page.domain;

import com.linking.block.domain.Block;
import com.linking.document.domain.Document;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.project.domain.Project;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "page")
@DiscriminatorValue("P")
@PrimaryKeyJoinColumn(name = "page_id")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends Document {

    @Column(length = 50)
    private String title;

    private LocalDateTime createdDatetime;

    private LocalDateTime updatedDatetime;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Block> blockList;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageCheck> pageCheckList;


    @Builder
    protected Page(int docIndex, Project project, Document parent, String title, LocalDateTime createdDatetime, LocalDateTime updatedDatetime, List<Block> blockList, List<PageCheck> pageCheckList) {
        super(docIndex, project, parent);
        this.title = title;
        this.createdDatetime = createdDatetime;
        this.updatedDatetime = updatedDatetime;
        this.blockList = blockList;
        this.pageCheckList = pageCheckList;
    }

    @PrePersist
    public void prePersist() {
        this.title = this.title == null ? "untitled" : this.title;
    }


    public void addBlock(Block block) {
        this.blockList.add(block);
        if (block.getPage() != this) {
            block.setPage(this);
        }
        this.updatedDatetime = LocalDateTime.now();
    }

    public void addPageCheck(PageCheck pageCheck) {
        this.pageCheckList.add(pageCheck);
        if (pageCheck.getPage() != this) {
            pageCheck.setPage(this);
        }
    }

    public void update(String title) {
        this.title = title;
        this.updatedDatetime = LocalDateTime.now();
    }

}
