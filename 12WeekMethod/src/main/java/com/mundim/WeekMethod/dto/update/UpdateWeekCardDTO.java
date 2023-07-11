package com.mundim.WeekMethod.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateWeekCardDTO(
        Long goalId,
        @Schema(defaultValue = "Description") String description,
        LocalDate weekStartDate,
        @Schema(defaultValue = "Notes") String notes
) {
}
