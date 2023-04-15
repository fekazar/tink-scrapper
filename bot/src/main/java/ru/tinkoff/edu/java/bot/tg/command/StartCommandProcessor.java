package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;
import ru.tinkoff.edu.java.bot.server.response.ApiErrorResponse;

@Component(StartCommandProcessor.START_COM)
public class StartCommandProcessor implements CommandProcessor {
    public static final String START_COM = "start";
    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String process(String command, String text, long chatId) {
        try {
            scrapperClient.registerChat(chatId);
            return "Welcome!";
        } catch (WebClientResponseException e) {
            return e.getResponseBodyAs(ApiErrorResponse.class).exceptionMessage(); // return textMessage instead, make it more verbose
        }
    }
}
