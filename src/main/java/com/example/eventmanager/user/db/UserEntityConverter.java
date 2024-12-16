package com.example.eventmanager.user.db;

import com.example.eventmanager.user.domain.UserRole;
import com.example.eventmanager.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserEntityConverter {

    public User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getLogin(),
                UserRole.valueOf(entity.getRole()),
                entity.getPassword(),
                entity.getAge()
        );
    }

    public UserEntity toEntity(User user) {
        return new UserEntity(
                user.id(),
                user.login(),
                user.role().name(),
                user.age(),
                user.passwordHash()
        );
    }
}
