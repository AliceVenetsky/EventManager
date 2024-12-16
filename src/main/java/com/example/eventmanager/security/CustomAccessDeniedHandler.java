package com.example.eventmanager.security;

import com.example.eventmanager.web.ServerErrorDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    private static final Logger log = LoggerFactory.getLogger(CustomAccessDeniedHandler.class);
    private final ObjectMapper mapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        log.error("Access denied {}", accessDeniedException.getMessage());
        ServerErrorDto errorDto = new ServerErrorDto(
                "Forbidden",
                accessDeniedException.getMessage(),
                LocalDateTime.now()
        );
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.getWriter().write(mapper.writeValueAsString(errorDto));
    }
}
