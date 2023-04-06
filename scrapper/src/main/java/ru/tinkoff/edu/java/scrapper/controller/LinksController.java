package ru.tinkoff.edu.java.scrapper.controller;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.tinkoff.edu.java.scrapper.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.response.LinkResponse;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/",
    produces = "application/json",
    consumes = "application/json")
public class LinksController {
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping(value = "/links")
    LinkResponse addLink(@RequestHeader AddLinkRequest request) {
        log.info("POST request to /links");
        return null;
    }

    @GetMapping(value = "/links")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    List<LinkResponse> getLink(@RequestHeader long tgChatId) {
        log.info("GET request to /links");
        return new ArrayList<>();
    }

    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @DeleteMapping(value = "/links")
    LinkResponse deleteLink(@RequestHeader long tgChatId,
                            @RequestBody RemoveLinkRequest request) {
        log.info("DELETE request for /links");
        return null;
    }
}
