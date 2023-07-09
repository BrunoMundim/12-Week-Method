package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;

@Entity
@Table(name = "week_card")
@Data
@NoArgsConstructor
public class WeekCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "week_card_id")
    private Long id;

    @Column(name = "goal_id")
    private Long goalId;

    @Column(name = "description")
    private String description;

    @Column(name = "week_start_date")
    private LocalDate weekStartDate;

    @Column(name = "week_end_date")
    private LocalDate weekEndDate;

    @Column(name = "week_tasks_ids")
    private HashSet<Long> weekTasksIds;

    @Column(name = "notes")
    private String notes;

    public WeekCard(WeekCardDTO dto){
        this.goalId = dto.goalId();
        this.description = dto.description();
        this.weekStartDate = dto.weekStartDate();
        this.weekEndDate = weekStartDate.plusDays(7);
        this.weekTasksIds = new HashSet<>();
        this.notes = dto.notes();
    }

}
