package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.scrapper.response.ApiErrorResponse;

@RestControllerAdvice
@Slf4j
public class ErrorsHandler {
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiErrorResponse> handle(Exception e) {
        long code = e instanceof ResponseStatusException rse ? rse.getStatusCode().value() : 400;

        log.error(e.getMessage());
        e.printStackTrace();

        return new ResponseEntity<>(new ApiErrorResponse("All exceptions are mapped to this response!",
                code,
                e.getClass().getName(),
                e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
