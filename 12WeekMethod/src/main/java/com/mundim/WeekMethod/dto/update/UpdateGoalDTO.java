package com.mundim.WeekMethod.dto.update;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Builder
public record UpdateGoalDTO(
        @Schema(defaultValue = "Title") String title,
        @Schema(defaultValue = "Description") String description,
        List<String> keyResultsDescription) {
}
