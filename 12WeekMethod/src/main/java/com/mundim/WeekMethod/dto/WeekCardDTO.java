package com.mundim.WeekMethod.dto;

import java.time.LocalDate;

public record WeekCardDTO(
        Long userId,
        String title,
        String description,
        LocalDate weekStartDate,
        LocalDate weekEndDate,
        String notes
) {
}
