package com.mundim.WeekMethod.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
public record UserDTO(String name, String email, String password) {
}
