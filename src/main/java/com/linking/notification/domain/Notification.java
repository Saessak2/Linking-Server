package com.linking.notification.domain;

import com.linking.project.domain.Project;
import com.linking.user.domain.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Notification {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private String sender;

    @Convert(converter = PriorityConverter.class)
    private String priority;

    private LocalDate createdDate;

    @Builder
    public Notification(User user, Project project, String sender, String priority) {
        this.user = user;
        this.project = project;
        this.sender = sender;
        this.priority = priority;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = this.createdDate == null ? LocalDate.now() : this.createdDate;
    }

    public String getCreatedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern("YY.MM.dd"));
    }

    public String getInfo() {
        return project.getProjectName() + " " + sender + " " + getCreatedDate();
    }
}
