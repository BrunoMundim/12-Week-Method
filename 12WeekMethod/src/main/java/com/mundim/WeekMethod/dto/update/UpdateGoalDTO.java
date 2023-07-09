package com.mundim.WeekMethod.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotNull;
import java.util.List;

public record UpdateGoalDTO(
        @Schema(defaultValue = "Title") String title,
        @Schema(defaultValue = "Description") String description,
        List<String> keyResultsDescription) {
}
