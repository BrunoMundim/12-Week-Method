package com.mundim.WeekMethod.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public record UpdateGoalDTO(
        @NotNull @Schema(defaultValue = "Title") String title,
        @NotNull @Schema(defaultValue = "Description") String description,
        @NotNull List<String> keyResultsDescription) {
}
