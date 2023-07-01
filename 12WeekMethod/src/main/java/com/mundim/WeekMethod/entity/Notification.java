package com.mundim.WeekMethod.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "message")
    private String message;

    @Column(name = "time_stamp")
    private LocalDate timeStamp;

    @Column(name = "read_status")
    private String readStatus;

}
