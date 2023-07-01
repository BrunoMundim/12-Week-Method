package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.GoalDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "goal")
@Data
@NoArgsConstructor
public class Goal {

    public enum StatusType{
        IN_PROGRESS,
        COMPLETED,
        ON_HOLD,
        NOT_STARTED,
        DEFERRED
    }

    @Embeddable
    @Data
    public static class KeyResult {
        private String description;
        private Double targetPercentage;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status")
    private StatusType status;

    @Column(name = "progress_percentage")
    private Double progressPercentage;

    @ElementCollection
    @CollectionTable(name = "goal_key_result", joinColumns = @JoinColumn(name = "goal_id"))
    private List<KeyResult> keyResults = new ArrayList<>();

    public Goal(GoalDTO goalDTO){
        this.userId = goalDTO.userId();
        this.title = goalDTO.title();
        this.description = goalDTO.description();
        this.startDate = goalDTO.startDate();
        this.endDate = goalDTO.endDate();
        if(goalDTO.status() == null)
            this.status = StatusType.NOT_STARTED;
        else this.status = goalDTO.status();
        this.progressPercentage = 0.0;
    }

}
