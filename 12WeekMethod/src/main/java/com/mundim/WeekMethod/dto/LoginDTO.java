package com.mundim.WeekMethod.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginDTO(
        @Schema(defaultValue = "email@email.com") String email,
        @Schema(defaultValue = "password") String password
) {
}
