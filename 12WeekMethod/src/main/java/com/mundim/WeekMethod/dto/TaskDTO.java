package com.mundim.WeekMethod.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskDTO(
        @NotNull Long weekCardId,
        @NotNull @Schema(defaultValue = "Title") String title,
        @NotNull @Schema(defaultValue = "Description") String description,
        LocalDate dueDate
) {
}
