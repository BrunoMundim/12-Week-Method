package com.mundim.WeekMethod.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record UserDTO(
        @Schema(defaultValue = "name") String name,
        @Schema(defaultValue = "email@email.com") String email,
        @Schema(defaultValue = "password") String password) {
}
