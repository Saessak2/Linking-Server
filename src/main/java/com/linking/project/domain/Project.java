package com.linking.project.domain;

import com.linking.document.domain.Document;
import com.linking.participant.domain.Participant;
import com.linking.user.domain.User;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;
import org.slf4j.Marker;

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

    @Column(name = "project_name", nullable = false, length = 28)
    private String projectName;

    @Column(name = "begin_date", nullable = false)
    private LocalDate beginDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(mappedBy = "project")
    private List<Participant> participants;

    public Project(Long projectId) {
        this.projectId = projectId;
    }

}
