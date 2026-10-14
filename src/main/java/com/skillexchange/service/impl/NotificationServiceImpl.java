package com.skillexchange.service.impl;

import com.skillexchange.dto.NotificationDto;
import com.skillexchange.entity.Notification;
import com.skillexchange.entity.User;
import com.skillexchange.exception.ResourceNotFoundException;
import com.skillexchange.exception.UnauthorizedException;
import com.skillexchange.repository.NotificationRepository;
import com.skillexchange.repository.UserRepository;
import com.skillexchange.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of NotificationService with asynchronous event dispatching.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public List<NotificationDto> getUserNotifications(String email) {
        User user = getUserByEmail(email);
        List<Notification> notifications = notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId());

        return notifications.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void markAsRead(String email, Long notificationId) {
        User user = getUserByEmail(email);
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id: " + notificationId));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedException("You are not authorized to modify this notification");
        }

        notification.setRead(true);
        notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public void markAllAsRead(String email) {
        User user = getUserByEmail(email);
        notificationRepository.markAllAsRead(user.getId());
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(String email) {
        User user = getUserByEmail(email);
        return notificationRepository.countByUserIdAndIsReadFalse(user.getId());
    }

    /**
     * Dispatches notification asynchronously to fulfill syllabus async requirement.
     * Non-blocking background worker thread records the notification in MySQL.
     */
    @Override
    @Async
    @Transactional
    public void sendAsyncNotification(User recipient, String title, String message, String type, Long referenceId) {
        try {
            logger.info("[ASYNC NOTIFICATION] Generating in-app notification for user ID {}: '{}'", recipient.getId(), title);
            Notification notification = new Notification(recipient, title, message, type, referenceId);
            notificationRepository.save(notification);
        } catch (Exception e) {
            logger.error("Failed to persist async notification for user ID {}: {}", recipient.getId(), e.getMessage());
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    private NotificationDto mapToDto(Notification n) {
        return new NotificationDto(
                n.getId(),
                n.getTitle(),
                n.getMessage(),
                n.getType(),
                n.getReferenceId(),
                n.isRead(),
                n.getCreatedAt()
        );
    }
}
