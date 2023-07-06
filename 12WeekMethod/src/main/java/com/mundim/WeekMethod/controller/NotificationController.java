package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.NotificationDTO;
import com.mundim.WeekMethod.entity.Notification;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.service.NotificationService;
import com.mundim.WeekMethod.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/notification")
@SecurityRequirement(name = "jwt")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDTO dto){
        verifyDtoForCreateNotification(dto);
        return new ResponseEntity<Notification>(notificationService.createNotification(dto), CREATED);
    }

    @GetMapping
    public ResponseEntity<Notification> findNotificationById(@RequestParam Long id){
        return ResponseEntity.ok(notificationService.findNotificationById(id));
    }

    @PutMapping
    public ResponseEntity<Notification> updateNotificationById(@RequestParam Long id, @RequestBody NotificationDTO dto){
        return ResponseEntity.ok(notificationService.updateNotificationById(id, dto));
    }

    @DeleteMapping
    public ResponseEntity<Notification> deleteNotificationById(@RequestParam Long id){
        return ResponseEntity.ok(notificationService.deleteNotificationById(id));
    }

    public void verifyDtoForCreateNotification(NotificationDTO dto) {
        userService.findUserById(dto.userId());
        if(dto.title() == null) throw new BadRequestException("Title field must not be null");
        if(dto.message() == null) throw new BadRequestException("Message field must not be null");
    }

}
