package com.linking.notification.persistence;

import com.linking.notification.domain.Notification;
import com.linking.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.createdDate < :threshold")
    List<Notification> findAllAgoMonth(@Param("threshold") LocalDate threshold);

    List<Notification> findAllByUser(User user);
}
