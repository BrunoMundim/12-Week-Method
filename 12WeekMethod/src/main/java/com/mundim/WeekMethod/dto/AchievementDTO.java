package com.mundim.WeekMethod.dto;

import java.time.LocalDate;

public record AchievementDTO(
        Long userId,
        String description,
        LocalDate dateAchieved
) {
}
