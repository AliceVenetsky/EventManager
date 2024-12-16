package com.example.eventmanager.user.domain;

import com.example.eventmanager.user.api.SignUpRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(
            UserService userService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }


    public User registerUser(SignUpRequest signInRequest) {
        if (userService.isUserFoundByLogin(signInRequest.login()))
            throw new IllegalArgumentException(
                    "User with login: %s already exists".formatted(signInRequest.login())
            );
        var passwordHash = passwordEncoder.encode(signInRequest.password());
        User newUser = new User(
                null,
                signInRequest.login(),
                UserRole.USER,
                passwordHash,
                signInRequest.age()
        );
        return userService.saveUser(newUser);
    }
}
