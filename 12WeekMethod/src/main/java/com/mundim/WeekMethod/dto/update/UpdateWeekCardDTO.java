package com.mundim.WeekMethod.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UpdateWeekCardDTO(
        @Schema(defaultValue = "Description") String description,
        LocalDate weekStartDate,
        @Schema(defaultValue = "Notes") String notes
) {
}
