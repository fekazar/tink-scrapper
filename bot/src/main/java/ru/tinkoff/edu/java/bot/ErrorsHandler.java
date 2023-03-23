package ru.tinkoff.edu.java.bot;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.bot.response.ApiErrorResponse;

@RestControllerAdvice
public class ErrorsHandler {

    // What is HttpMessageNotReadableException really responsible for?
    // Is there a better way to handle "internal" exceptions?
    // Or there enough information in exception message?
    @ExceptionHandler({ResponseStatusException.class,
            HttpMessageNotReadableException.class})
    ApiErrorResponse handleBadRequest(Exception e) {
        return new ApiErrorResponse("Placeholder",
                400,
                e.getClass().getName(),
                e.getMessage());
    }
}
