package com.juu.juulabel.domain.repository.jpa;

import com.juu.juulabel.domain.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationJpaRepository extends JpaRepository<Notification, Long> {
}
