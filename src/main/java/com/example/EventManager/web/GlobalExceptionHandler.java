package com.example.EventManager.web;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;


@ControllerAdvice
public class GlobalExceptionHandler {
    private final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(value = {
            IllegalArgumentException.class,
            MethodArgumentNotValidException.class
    })
    public ResponseEntity<ServerErrorDto> handleArgumentException(Exception e) {
        log.error("Get not valid exception", e);
        String detailedMessage = e instanceof MethodArgumentNotValidException ?
                getValidationExceptionMessage((MethodArgumentNotValidException)e):e.getMessage();

        var errorDto =  new ServerErrorDto(
                "Ошибка валидации",
                detailedMessage,
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }

    @ExceptionHandler(value =  EntityNotFoundException.class)
    public ResponseEntity<ServerErrorDto> handleNotFoundException(EntityNotFoundException e) {
        log.error("Get exception entity not found", e);
        var errorDto =  new ServerErrorDto(
                "Сущность не найдена",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorDto);
    }

    @ExceptionHandler
    public ResponseEntity<ServerErrorDto> handleGenericException(Exception e) {
        log.error("Get server exception", e);
        var errorDto =  new ServerErrorDto(
                "Ошибка сервера",
                e.getMessage(),
                LocalDateTime.now());
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorDto);
    }

    private String getValidationExceptionMessage(MethodArgumentNotValidException e) {
        String detailedMessage = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return detailedMessage;
    }
}
