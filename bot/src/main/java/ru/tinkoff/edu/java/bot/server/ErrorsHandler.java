package ru.tinkoff.edu.java.bot.server;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.bot.server.response.ApiErrorResponse;

@RestControllerAdvice
public class ErrorsHandler {
    @ExceptionHandler({ResponseStatusException.class,
            HttpMessageNotReadableException.class})
    ApiErrorResponse handleBadRequest(Exception e) {
        return new ApiErrorResponse("Placeholder",
            HttpStatus.BAD_REQUEST.value(),
                e.getClass().getName(),
                e.getMessage());
    }
}
