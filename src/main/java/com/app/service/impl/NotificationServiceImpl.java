
package com.app.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.app.entity.Notification;
import com.app.repository.NotificationRepository;
import com.app.service.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification createNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(userId);
    }
}
