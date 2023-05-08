package com.linking.push_notification.domain;

import com.linking.project.domain.Project;
import com.linking.user.domain.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PushNotification {

    @Id @GeneratedValue
    @Column(name = "push_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    private Long target_id;

    private String sender;

    @Enumerated(value = EnumType.STRING)
    private NoticeType noticeType;

    private int priority;

    private LocalDate createdDate;

    @Setter
    private boolean isChecked;

    private String body;

    public PushNotification(User user, Project project, Long target_id, String sender, NoticeType noticeType, int priority, String body) {
        this.user = user;
        this.project = project;
        this.target_id = target_id;
        this.sender = sender;
        this.noticeType = noticeType;
        this.priority = priority;
        this.body = body;
    }

    @PrePersist
    public void prePersist() {
        this.createdDate = this.createdDate == null ? LocalDate.now() : this.createdDate;
    }

    public String getInfo() {
        return project.getProjectName() + " " + sender + " " + getCreatedDate();
    }

    public String getCreatedDate() {
        return createdDate.format(DateTimeFormatter.ofPattern("YY.MM.dd"));
    }
}
