package com.example.eventmanager.user.api;

import com.example.eventmanager.user.domain.AuthenticationService;
import com.example.eventmanager.user.domain.User;
import com.example.eventmanager.user.domain.UserRegistrationService;
import com.example.eventmanager.user.domain.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {


    private final static Logger log = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;
    private final UserRegistrationService registrationService;
    private final AuthenticationService authenticationServer;


    public UserController(
            UserService userService,
            UserRegistrationService registrationService,
            AuthenticationService authenticationServer
    ) {
        this.userService = userService;
        this.registrationService = registrationService;
        this.authenticationServer = authenticationServer;
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        log.info("Get request for sign-in login = {}", signUpRequest.login());
        var newUser = registrationService.registerUser(signUpRequest);

        return ResponseEntity
                .status(201)
                .body(toUserDto(newUser));
    }

    @PostMapping("/auth")
    public ResponseEntity<JwtTokenResponse> authenticate(
            @RequestBody @Valid SignInRequest signInRequest
    ) {
        String token = authenticationServer.authenticateUser(signInRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new JwtTokenResponse(token));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserInfoById(
            @PathVariable("userId") Long userId) {

        log.info("Get user info by id = {} ", userId);
        var foundUser = userService.getUserById(userId);
        return ResponseEntity.ok().body(toUserDto(foundUser));
    }

    public UserDto toUserDto(User user) {
        return new UserDto(
                user.id(),
                user.login(),
                user.role(),
                user.age()
        );
    }
}
