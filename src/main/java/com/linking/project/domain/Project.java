package com.linking.project.domain;

import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@Table(name = "project")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long projectId;

    @Column(name = "project_name")
    private String projectName;

    @Column(name = "begin_date")
    private LocalDate beginDate;

    @Column(name = "due_date")
    private LocalDate dueDate;

    // TODO: NESTED ERROR ? (Part needs proj -> Proj needs Part)
    @OneToMany(mappedBy = "project")
    private final List<Participant> participantList = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    public Project(Long projectId) {
        this.projectId = projectId;
    }

}
