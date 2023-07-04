package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.AchievementDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "achievement")
@Data
@NoArgsConstructor
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

    public Achievement(AchievementDTO dto) {
        this.userId = dto.userId();
        this.description = dto.description();
        this.dateAchieved = dto.dateAchieved();
    }
}
