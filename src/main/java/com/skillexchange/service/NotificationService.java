package com.skillexchange.service;

import com.skillexchange.dto.NotificationDto;
import com.skillexchange.entity.User;

import java.util.List;

/**
 * Service interface for user notifications.
 */
public interface NotificationService {

    List<NotificationDto> getUserNotifications(String email);

    void markAsRead(String email, Long notificationId);

    void markAllAsRead(String email);

    long getUnreadCount(String email);

    void sendAsyncNotification(User recipient, String title, String message, String type, Long referenceId);
}
