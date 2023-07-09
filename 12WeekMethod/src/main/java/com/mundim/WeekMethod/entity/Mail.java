package com.mundim.WeekMethod.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.mail.SimpleMailMessage;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "mail")
@Data
@NoArgsConstructor
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mail_id")
    private Long id;

    @Column(name = "email_from")
    private String from;

    @Column(name = "email_to")
    private String to;

    @Column(name = "subject", length = 10000)
    private String subject;

    @Column(name = "message", length = 30000)
    private String message;

    @Column(name = "time_stamp")
    private LocalDate timeStamp;

    public Mail(String from, String to, String subject, String message) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.message = message;
        this.timeStamp = LocalDate.now();
    }

}
