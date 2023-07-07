package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.TaskDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "task")
@Data
@NoArgsConstructor
public class Task {

    public enum TaskStatus{
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "task_id")
    private Long id;

    @Column(name = "task_week_card_id")
    private Long weekCardId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @Column(name = "status")
    private TaskStatus status;

    public Task(TaskDTO dto) {
        this.weekCardId = dto.weekCardId();
        this.title = dto.title();
        this.description = dto.description();
        this.dueDate = dto.dueDate();
        this.status = TaskStatus.NOT_STARTED;
    }
}
