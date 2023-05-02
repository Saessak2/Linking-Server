package com.linking.notification.persistence;

import com.linking.notification.domain.Notification;
import com.linking.project.domain.Project;
import com.linking.project.persistence.ProjectRepository;
import com.linking.user.domain.User;
import com.linking.user.persistence.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
        User user = userRepository.getReferenceById(4L);
        Project project = projectRepository.getReferenceById(3L);

        for (int i = 0; i < 8; i++) {
            Notification notification = new Notification(user, project, "서민정", "ALL");
            notificationRepository.save(notification);
        }
//        Notification notification2 = Notification.builder()
//                .user(user)
//                .project(project)
//                .priority("NO_MAIL")
//                .sender("서민정")
//                .build();
//        Notification save2 = notificationRepository.save(notification2);

//        assertThat(notification).isEqualTo(save1);
//        assertThat(notification2).isEqualTo(save2);
    }

    @Test
    @Rollback(value = false)
    void delete() {  // 한달이 지난 알람 삭제
        List<Notification> notifications = notificationRepository.findAllAgoMonth(LocalDate.now().minusMonths(1));
        notificationRepository.deleteAll(notifications);
    }

    @Test
    @Rollback
    void findAllByUser() {
        User user = userRepository.getReferenceById(4L);
        List<Notification> allByUser = notificationRepository.findAllByUser(user);
        Assertions.assertEquals(8, allByUser.size());
    }


}