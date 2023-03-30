package com.linking.page;

import com.linking.block.Block;
import com.linking.group.Group;
import com.linking.pageCheck.PageCheck;
import com.linking.project.Project;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "page")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Page  {
    /**
     * field
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "page_id")
    private Long id;

    @Column(length = 50)
    private String title;

    private int pageIndex;

    @NotNull
    private LocalDateTime createdDatetime;

    @NotNull
    private LocalDateTime updatedDatetime;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;
    @OneToMany(mappedBy = "page", cascade = CascadeType.ALL)
    private List<Block> blockList;

    @OneToMany(mappedBy = "page", cascade = CascadeType.REMOVE)
    private List<PageCheck> pageCheckList;

    /**
     * constructor
     */

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

    public void setGroup(Group group) {
        this.group = group;
        if (!group.getPageList().contains(this)) {
            group.getPageList().add(this);
        }
    }

    public void setProject(Project project) {
        this.project = project;
        if (!project.getPageList().contains(this)) {
            project.getPageList().add(this);
        }
    }
}
