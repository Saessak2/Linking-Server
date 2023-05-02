package com.linking.notification.persistence;

import com.linking.notification.domain.Notification;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import static org.assertj.core.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
class NotificationRepositoryTest {

    @Autowired
    private NotificationRepository notificationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @Rollback(value = false)
    void save() {
        User user = userRepository.getReferenceById(1L);
        Project project = projectRepository.getReferenceById(3L);
        Notification notification = Notification.builder()
                .user(user)
                .project(project)
                .priority("ALL") // NO_MAIL
                .sender("서민정")
                .build();
        Notification save1 = notificationRepository.save(notification);
        Notification notification2 = Notification.builder()
                .user(user)
                .project(project)
                .priority("NO_MAIL")
                .sender("서민정")
                .build();
        Notification save2 = notificationRepository.save(notification2);

        assertThat(notification).isEqualTo(save1);
        assertThat(notification2).isEqualTo(save2);
    }

    @Test
    @Rollback(value = false)
    void delete() {

    }
}