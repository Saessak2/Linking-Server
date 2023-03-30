package com.linking.page;

import com.linking.block.Block;
import com.linking.document.Document;
import com.linking.pageCheck.PageCheck;
import com.linking.project.Project;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("P")
@PrimaryKeyJoinColumn(name = "page_id")
@Table(name = "page")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page extends Document {
    /**
     * field
     */

    @Column(length = 50)
    private String title;
    @NotNull
    private LocalDateTime createdDatetime;
    @NotNull
    private LocalDateTime updatedDatetime;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Block> blockList;

    @OneToMany(mappedBy = "page", cascade = CascadeType.REMOVE)
    private List<PageCheck> pageCheckList;

    /**
     * constructor
     */
    @Builder
    public Page(String title, int doc_depth, int doc_index, Project project, LocalDateTime createdDatetime, LocalDateTime updatedDatetime, List<Block> blockList, List<PageCheck> pageCheckList) {
        super(doc_depth, doc_index, project);
        this.title = title;
        this.createdDatetime = createdDatetime;
        this.updatedDatetime = updatedDatetime;
        this.blockList = blockList;
        this.pageCheckList = pageCheckList;
    }

    /**
     * method
     */

    public void addBlock(Block block) {
        this.blockList.add(block);
        if (block.getPage() != this) {
            block.setPage(this);
        }
    }


    public void addPageCheck(PageCheck pageCheck) {
        this.pageCheckList.add(pageCheck);
        if (pageCheck.getPage() != this) {
            pageCheck.setPage(this);
        }
    }


}
