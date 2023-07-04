package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.NotificationDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.ast.Not;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
public class Notification {

    public enum ReadStatus {
        READED,
        NOT_READED
    }

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
    private ReadStatus readStatus;

    public Notification(NotificationDTO dto){
        this.userId = dto.userId();
        this.title = dto.title();
        this.message = dto.message();
        this.timeStamp = LocalDate.now();
        this.readStatus = ReadStatus.NOT_READED;
    }

}
