package com.linking.page.domain;

import com.linking.block.domain.Block;
import com.linking.group.domain.Group;
import com.linking.pageCheck.domain.PageCheck;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
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

    private int pageOrder;

    private String title;

    @Enumerated(value = EnumType.STRING)
    private Template template;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    @OrderBy("blockOrder asc")
    private List<Block> blockList;

    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<PageCheck> pageCheckList;


    @Builder
    public Page(int pageOrder, String title, List<Block> blockList, List<PageCheck> pageCheckList, Template template) {
        this.pageOrder = pageOrder;
        this.title = title;
        this.template = template;
        this.blockList = blockList;
        this.pageCheckList = pageCheckList;
    }

    public void setGroup(Group group) {
        this.group = group;
        if (!group.getPageList().contains(this)) {
            group.getPageList().add(this);
        }
    }

    @PrePersist
    public void prePersist(){
        this.title = this.title == null ? "untitled" : this.title;
        this.blockList = this.blockList == null ? new ArrayList<>() : this.blockList;
        this.pageCheckList = this.pageCheckList == null ? new ArrayList<>() : this.pageCheckList;
    }

    public void updateOrder(int order) {
        this.pageOrder = order;
    }
}
