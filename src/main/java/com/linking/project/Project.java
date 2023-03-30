package com.linking.project;

import com.linking.group.Group;
import com.linking.page.Page;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "project")
@Builder
@AllArgsConstructor
public class Project {

    /**
     * field
     */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Group> groupList;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Page> pageList;

    /**
     * constructor
     */


    /**
     * method
     */

    public void addGroup(Group group) {
        this.groupList.add(group);
        if (group.getProject() != this) {
            group.setProject(this);
        }
    }

    public void addPage(Page page) {
        this.pageList.add(page);
        if (page.getProject() != this) {
            page.setProject(this);
        }
    }
}
