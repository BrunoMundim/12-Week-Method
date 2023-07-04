package com.mundim.WeekMethod.dto;

public record NotificationDTO(
        Long userId,
        String title,
        String message
) {
}
