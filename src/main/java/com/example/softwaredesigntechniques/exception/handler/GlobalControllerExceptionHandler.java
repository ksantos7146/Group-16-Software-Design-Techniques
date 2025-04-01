package com.example.softwaredesigntechniques.exception.handler;


import com.example.softwaredesigntechniques.dto.exception.NotFoundErrorResponseDto;
import com.example.softwaredesigntechniques.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
class GlobalControllerExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    private static final String RESOURCE_NOT_FOUND = "Resource not found";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    NotFoundErrorResponseDto handleNotFoundErrorResponseDto(NotFoundException exception) {
        String message = getMessage(exception, RESOURCE_NOT_FOUND);

        return new NotFoundErrorResponseDto(message);
    }


    private String getMessage(Exception exception, String defaultMessage) {
        String message = defaultMessage;

        if (Objects.nonNull(exception.getLocalizedMessage())) {
            message = exception.getLocalizedMessage();
        }
        if (Objects.nonNull(exception.getMessage())) {
            message = exception.getMessage();
        }

        return message;
    }
}
