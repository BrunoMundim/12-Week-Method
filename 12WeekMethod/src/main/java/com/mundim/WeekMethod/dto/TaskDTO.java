package com.mundim.WeekMethod.dto;

import java.time.LocalDate;

public record TaskDTO(
        Long weekCardId,
        String title,
        String description,
        LocalDate dueDate
) {
}
