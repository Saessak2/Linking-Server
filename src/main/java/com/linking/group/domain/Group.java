package com.linking.group.domain;

import com.linking.page.domain.Page;
import com.linking.project.domain.Project;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "\"group\"")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Group  {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "group_id")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private int order;

    @OneToMany(mappedBy = "group")
    private List<Page> pageList;


    @Builder
    public Group(String name, Project project, int order) {
        this.name = name;
        this.project = project;
        this.order = order;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void addGroup(Page page) {
        this.pageList.add(page);
        if (page.getGroup() != this) {
            page.setGroup(this);
        }
    }

    @PrePersist
    public void prePersist() {
        this.name = this.name == null ? "New Group" : this.name;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void changeOrder(int order) {
        this.order = order;
    }
}

