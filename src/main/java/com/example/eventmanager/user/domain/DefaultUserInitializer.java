package com.example.eventmanager.user.domain;

import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DefaultUserInitializer {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public DefaultUserInitializer(
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void initDefaultUsers() {
        createUserIfNotExists("admin", "admin123", UserRole.ADMIN);
        createUserIfNotExists("user", "user123", UserRole.USER);
    }

    private void createUserIfNotExists(
            String login,
            String password,
            UserRole role
    ) {
        if (userService.isUserFoundByLogin(login))
            return;

        User user = new User(
                null,
                login,
                role,
                passwordEncoder.encode(password),
                19
        );
        userService.saveUser(user);
    }
}
