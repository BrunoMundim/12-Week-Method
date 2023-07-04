package com.mundim.WeekMethod.repository;

import com.mundim.WeekMethod.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
