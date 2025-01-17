package com.example.eventmanager.user.domain;

import com.example.eventmanager.security.JwtTokenManager;
import com.example.eventmanager.user.api.SignInRequest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final JwtTokenManager jwtTokenManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    public AuthenticationService(
            JwtTokenManager jwtTokenManager,
            PasswordEncoder passwordEncoder,
            UserService userService
    ) {

        this.jwtTokenManager = jwtTokenManager;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    public String authenticateUser(SignInRequest signInRequest) {
        if (!userService.isUserFoundByLogin(signInRequest.login()))
            throw new BadCredentialsException("Problem with user");

        var user = userService.getUserByLogin(signInRequest.login());
        if (!passwordEncoder.matches(signInRequest.password(), user.passwordHash()))
            throw new BadCredentialsException("Problem with password");

        return jwtTokenManager.generateJwtToken(user);
    }

    public User getCurrentUser() {
        return (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
