package com.linking.push_notification.persistence;

import com.linking.push_notification.domain.PushNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PushNotificationRepository extends JpaRepository<PushNotificationRepository, Long> {


    @Query("SELECT n FROM PushNotification n WHERE PushNotification.user.userId = :userId")
    List<PushNotification> findAllByUserId(@Param("userId") Long userId);
}
