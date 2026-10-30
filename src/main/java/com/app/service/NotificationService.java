
package com.app.service;

import java.util.List;
import com.app.entity.Notification;

public interface NotificationService {
    Notification createNotification(Notification notification);
    List<Notification> getNotificationsByUserId(Long userId);
}
