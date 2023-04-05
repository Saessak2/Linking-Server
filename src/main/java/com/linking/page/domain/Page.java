package com.linking.page.domain;

import com.linking.block.domain.Block;
import com.linking.document.domain.Document;
import com.linking.pageCheck.domain.PageCheck;
import com.linking.project.domain.Project;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "page")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    private LocalDateTime createdDatetime;

    private LocalDateTime updatedDatetime;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Block> blockList;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageCheck> pageCheckList;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "document_id")
    private Document document;

    @Builder
    public Page(LocalDateTime createdDatetime, LocalDateTime updatedDatetime, List<Block> blockList, List<PageCheck> pageCheckList, Document document) {
        this.createdDatetime = createdDatetime;
        this.updatedDatetime = updatedDatetime;
        this.blockList = blockList;
        this.pageCheckList = pageCheckList;
        this.document = document;
    }


    public void setDocument(Document document) {
        this.document = document;
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
}
