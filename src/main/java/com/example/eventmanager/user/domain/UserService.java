package com.example.eventmanager.user.domain;

import com.example.eventmanager.user.db.UserEntityConverter;
import com.example.eventmanager.user.db.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserEntityConverter entityConverter;

    public UserService(UserRepository userRepository,
                       UserEntityConverter entityConverter
    ) {
        this.userRepository = userRepository;
        this.entityConverter = entityConverter;
    }

    public boolean isUserFoundByLogin(String login) {
        return userRepository
                .findByLogin(login)
                .isPresent();
    }

    public User saveUser(User user) {
        log.info("Save user = {} ", user);
        var savedUser = userRepository.save(entityConverter.toEntity(user));
        return entityConverter.toDomain(savedUser);
    }

    public User getUserByLogin(String login) {
        log.info("Get user by login = {} ", login);
        return userRepository.findByLogin(login)
                .map(entityConverter::toDomain)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with login: %s not found".formatted(login))
                );
    }

    public User getUserById(Long id) {
        log.info("Get user by id = {} ", id);
        return userRepository.findById(id)
                .map(entityConverter::toDomain)
                .orElseThrow(
                        () -> new EntityNotFoundException("User with id: %s not found".formatted(id))
                );
    }
}
