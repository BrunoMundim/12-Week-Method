package com.mundim.WeekMethod.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record WeekCardDTO(
        @NotNull Long goalId,
        @NotNull @Schema(defaultValue = "Description") String description,
        @NotNull LocalDate weekStartDate,
        @NotNull @Schema(defaultValue = "Notes") String notes
) {
}
