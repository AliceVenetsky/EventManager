package com.example.eventmanager.web;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public record ServerErrorDto(
        String message,
        String detailedMessage,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "YYYY-MM-dd'T'hh:mm:ss", timezone = "EST")
        LocalDateTime dateTime
) {

}
