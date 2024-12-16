package com.example.eventmanager.user.domain;

public record User(
        Long id,
        String login,
        UserRole role,
        String passwordHash,
        Integer age
) {
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", passwordHash=***" +
                ", age=" + age +
                '}';
    }
}
