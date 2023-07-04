package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.WeekCardDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Entity
@Table(name = "week_card")
@Data
@NoArgsConstructor
public class WeekCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "description")
    private String description;

    @Column(name = "week_start_date")
    private LocalDate weekStartDate;

    @Column(name = "week_end_date")
    private LocalDate weekEndDate;

    @Column(name = "week_tasks_ids")
    private HashSet<Long> weekTasksIds;

    @Column(name = "completed_tasks")
    private Integer completedTasks;

    @Column(name = "pending_tasks")
    private Integer pendingTasks;

    @Column(name = "notes")
    private String notes;

    public WeekCard(WeekCardDTO dto){
        this.userId = dto.userId();
        this.description = dto.description();
        this.weekStartDate = dto.weekStartDate();
        this.weekEndDate = dto.weekEndDate();
        this.weekTasksIds = new HashSet<>();
        this.completedTasks = 0;
        this.pendingTasks = 0;
        this.notes = dto.notes();
    }

}
