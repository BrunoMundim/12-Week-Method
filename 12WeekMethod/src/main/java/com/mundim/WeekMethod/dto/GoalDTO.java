package com.mundim.WeekMethod.dto;

import com.mundim.WeekMethod.entity.Goal;

import java.time.LocalDate;

public record GoalDTO(Long userId,
                      String title,
                      String description,
                      LocalDate startDate,
                      LocalDate endDate,
                      Goal.StatusType status) {
}
