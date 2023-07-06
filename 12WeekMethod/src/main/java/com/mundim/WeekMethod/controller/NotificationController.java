package com.mundim.WeekMethod.controller;

import com.mundim.WeekMethod.dto.NotificationDTO;
import com.mundim.WeekMethod.entity.Notification;
import com.mundim.WeekMethod.exception.BadRequestException;
import com.mundim.WeekMethod.service.NotificationService;
import com.mundim.WeekMethod.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/notification")
@Api(tags = "notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UserService userService;

    public NotificationController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }

    @PostMapping
    @ApiOperation("Create a notification")
    public ResponseEntity<Notification> createNotification(@RequestBody NotificationDTO dto){
        verifyDtoForCreateNotification(dto);
        return new ResponseEntity<Notification>(notificationService.createNotification(dto), CREATED);
    }

    @GetMapping
    @ApiOperation("Find a notification by ID")
    public ResponseEntity<Notification> findNotificationById(@RequestParam Long id){
        return ResponseEntity.ok(notificationService.findNotificationById(id));
    }

    @PutMapping
    @ApiOperation("Update a notification by ID")
    public ResponseEntity<Notification> updateNotificationById(@RequestParam Long id, @RequestBody NotificationDTO dto){
        return ResponseEntity.ok(notificationService.updateNotificationById(id, dto));
    }

    @DeleteMapping
    @ApiOperation("Delete a notification by ID")
    public ResponseEntity<Notification> deleteNotificationById(@RequestParam Long id){
        return ResponseEntity.ok(notificationService.deleteNotificationById(id));
    }

    public void verifyDtoForCreateNotification(NotificationDTO dto) {
        userService.findUserById(dto.userId());
        if(dto.title() == null) throw new BadRequestException("Title field must not be null");
        if(dto.message() == null) throw new BadRequestException("Message field must not be null");
    }

}
