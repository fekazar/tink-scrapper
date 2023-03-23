package ru.tinkoff.edu.java.bot;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.tinkoff.edu.java.bot.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.response.ApiErrorResponse;

@RestController
@RequestMapping(value = "/", consumes = "application/json", produces = "application/json")
public class BotController {
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/updates")
    void postUpdates(@RequestBody LinkUpdateRequest request) {

    }
}
