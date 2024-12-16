package com.example.eventmanager.user.api;

import com.example.eventmanager.user.domain.UserRole;

public record UserDto(
        Long id,
        String login,
        UserRole role,
        Integer age
) {
}
