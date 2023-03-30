package com.linking.group;

import com.linking.page.Page;
import com.linking.project.Project;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    private Long id;
    @Column(length = 10)
    private String name;

    private int groupIndex;

    @ManyToOne
    @JoinColumn(name = "project_id")
    private Project project;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Page> pageList;

    /**
     * constructor
     */



    /**
     * method
     */

    @PrePersist
    public void prePersist() {
        this.name = this.name == null ? "untitled" : this.name;
    }

    public void setProject(Project project) {
        this.project = project;
        if (!project.getGroupList().contains(this)) {
            project.getGroupList().add(this);
        }
    }

    public void addPage(Page page) {
        this.pageList.add(page);
        if (page.getGroup() != this) {
            page.setGroup(this);
        }
    }
}

