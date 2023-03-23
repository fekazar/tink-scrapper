package ru.tinkoff.edu.java.scrapper;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.response.ApiErrorResponse;

@RestControllerAdvice
public class ErrorsHandler {

    // What is HttpMessageNotReadableException really responsible for?
    // Is there a better way to handle "internal" exceptions?
    // Or there enough information in exception message?
    @ExceptionHandler({ResponseStatusException.class,
            HttpMessageNotReadableException.class})
    ApiErrorResponse handle(Exception e) {
        long code = e instanceof ResponseStatusException rse ? rse.getStatusCode().value() : 400;

        return new ApiErrorResponse("Placeholder",
                code,
                e.getClass().getName(),
                e.getMessage());
    }
}
