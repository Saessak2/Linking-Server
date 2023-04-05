package com.linking.page.domain;

import com.linking.block.domain.Block;
import com.linking.group.domain.Group;
import com.linking.pageCheck.domain.PageCheck;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "page")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Page {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "page_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    private int order;

    private String title;
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Block> blockList;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageCheck> pageCheckList;


    @Builder
    public Page(Group group, int order, String title, List<Block> blockList, List<PageCheck> pageCheckList) {
        this.group = group;
        this.order = order;
        this.title = title;
        this.blockList = blockList;
        this.pageCheckList = pageCheckList;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

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

    @PrePersist
    public void prePersist(){
        this.title = this.title == null ? "untitled" : this.title;
    }

    public void updateOrder(int order) {
        this.order = order;
    }

}
