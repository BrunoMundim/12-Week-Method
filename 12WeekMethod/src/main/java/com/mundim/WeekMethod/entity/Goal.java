package com.mundim.WeekMethod.entity;

import com.mundim.WeekMethod.dto.GoalDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@AllArgsConstructor
@Builder
public class Goal {

    public enum StatusType {
        NOT_STARTED,
        IN_PROGRESS,
        COMPLETED
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

    @Column(name = "completion_date")
    private LocalDate completionDate;

    @Column(name = "status")
    private StatusType status;

    @Column(name = "progress_percentage")
    private Double progressPercentage;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "goal_id")
    private List<KeyResult> keyResults = new ArrayList<>();

    public Goal(GoalDTO goalDTO, Long userId) {
        this.userId = userId;
        this.title = goalDTO.title();
        this.description = goalDTO.description();
        this.startDate = null;
        this.endDate = null;
        this.completionDate = null;
        this.status = StatusType.NOT_STARTED;
        this.progressPercentage = 0.0;
        for (int i = 0; i < goalDTO.keyResultsDescription().size(); i++) {
            keyResults.add(new KeyResult(goalDTO.keyResultsDescription().get(i)));
        }
    }

    public Double calculateProgressPercentage() {
        double contCompleted = 0.0;
        for (KeyResult keyResult : keyResults) {
            if (keyResult.isCompleted()) contCompleted++;
        }
        return contCompleted / keyResults.size();
    }
}
