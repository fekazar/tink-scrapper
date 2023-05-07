package ru.tinkoff.edu.java.scrapper.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.service.ChatService;

@Slf4j
@RestController
@RequestMapping(value = "/tg-chat",
    consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class TgChatController {
    private static final String basePath = "/tg-chat"; // for logging

    @Autowired
    private ChatService chatService;

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/{id}")
    void registerChat(@PathVariable long id) {
        log.info("POST request to " + basePath + "/{id}");
        chatService.add(id);
    }

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping("/{id}")
    void deleteChat(@PathVariable long id) {
        log.info("DELETE request to " + basePath + "/{id}");
        chatService.remove(id);
    }
}
