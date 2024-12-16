package com.example.eventmanager.user.api;

import jakarta.validation.constraints.NotBlank;

public record SignInRequest(
        @NotBlank
        String login,
        @NotBlank
        String password
) {
}
