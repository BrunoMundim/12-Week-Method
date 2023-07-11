package com.mundim.WeekMethod.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "goal_key_result")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KeyResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "key_result_id")
    private Long id;

    private String description;

    private boolean completed;

    public KeyResult(String description) {
        this.description = description;
        this.completed = false;
    }

}
