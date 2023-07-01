package com.mundim.WeekMethod.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "week_card")
@Data
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

    @Column(name = "completed_tasks")
    private Integer completedTasks;

    @Column(name = "pending_tasks")
    private Integer pendingTasks;

    @Column(name = "notes")
    private String notes;

}
