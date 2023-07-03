package com.mundim.WeekMethod.dto;

import java.time.LocalDate;
import java.util.HashSet;

public record WeekCardDTO(
        Long userId,
        String title,
        String description,
        LocalDate weekStartDate,
        LocalDate weekEndDate,
        HashSet<Long> weekTasksIds,
        String notes
) {
}
