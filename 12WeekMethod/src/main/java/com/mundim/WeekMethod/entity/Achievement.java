package com.mundim.WeekMethod.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "achievement")
@Data
public class Achievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "achievement_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "date_achieved")
    private LocalDate dateAchieved;

}
