package ru.tinkoff.edu.java.bot.server;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.tinkoff.edu.java.bot.server.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.server.response.ApiErrorResponse;
import ru.tinkoff.edu.java.bot.tg.Bot;

@Slf4j
@RestController
@RequestMapping(value = "/", consumes = "application/json", produces = "application/json")
public class BotController {
    @Autowired
    private Bot bot;

    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "400", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    @PostMapping("/updates")
    void postUpdates(@RequestBody LinkUpdateRequest request) {
        log.info("POST request to /updates");
        bot.sendTextMessage(request.textMessage(), request.tgChatId());
    }
}
