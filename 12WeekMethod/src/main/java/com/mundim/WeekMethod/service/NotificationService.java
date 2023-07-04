package com.mundim.WeekMethod.service;

import com.mundim.WeekMethod.dto.NotificationDTO;
import com.mundim.WeekMethod.entity.Notification;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserService userService;

    public NotificationService(NotificationRepository notificationRepository, UserService userService) {
        this.notificationRepository = notificationRepository;
        this.userService = userService;
    }

    public Notification createNotification(NotificationDTO dto){
        verifyDtoInfo(dto);
        return notificationRepository.save(new Notification(dto));
    }

    public Notification findNotificationById(Long id){
        return notificationRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Notification not found"));
    }

    public Notification updateNotificationById(Long id, NotificationDTO dto){
        Notification notification = findNotificationById(id);
        if(dto.userId() != null) {
            userService.findUserById(dto.userId());
            notification.setUserId(dto.userId());
        }
        if(dto.title() != null) notification.setTitle(dto.title());
        if(dto.message() != null) notification.setMessage(dto.message());
        return notificationRepository.save(notification);
    }

    public Notification deleteNotificationById(Long id){
        Notification notification = findNotificationById(id);
        notificationRepository.deleteById(id);
        return notification;
    }

    public void verifyDtoInfo(NotificationDTO dto){
        userService.findUserById(dto.userId());
        if(dto.title() == null) throw new BadRequestException("Title field cannot be null");
        if(dto.message() == null) throw new BadRequestException("Message field cannot be null");
    }

}
