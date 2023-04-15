package ru.tinkoff.edu.java.bot.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import ru.tinkoff.edu.java.scrapper.request.AddLinkRequest;
import ru.tinkoff.edu.java.scrapper.request.RemoveLinkRequest;
import ru.tinkoff.edu.java.scrapper.response.ApiErrorResponse;
import ru.tinkoff.edu.java.scrapper.response.LinkResponse;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

// For simplicity this client will be blocking
// The one who is requesting the object should catch the exception

@Component
public class ScrapperClient {

    @Autowired
    @Qualifier("scrapperWebClient")
    private final WebClient client;

    public ScrapperClient(WebClient client) {
        this.client = client;
    }

    public LinkResponse addLink(URL url, long chatId) {
        var request = new AddLinkRequest(url);
        return client.post()
                .uri(uriBuilder -> uriBuilder.pathSegment("links").build())
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("tgChatId", String.valueOf(chatId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    public LinkResponse deleteLink(long tgChatId, URL url) {
        return client.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.pathSegment("links").build())
                .header("tgChatId", String.valueOf(tgChatId))
                .bodyValue(new RemoveLinkRequest(url))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    public LinkResponse[] getLinks(long tgChatId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("links")
                        .build())
                .header("tgChatId", String.valueOf(tgChatId))
                .header("Content-type", "application/json")
                .retrieve()
                .bodyToMono(LinkResponse[].class)
                .block();
    }

    public void registerChat(long tgChatId) {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("tg-chat")
                        .pathSegment(String.valueOf(tgChatId)).build())
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void deleteChat(long tgChatId) {
        client.delete()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("links")
                        .pathSegment(String.valueOf(tgChatId)).build())
                .header("content-type", MediaType.APPLICATION_JSON_VALUE)
                .header("tgChatId", String.valueOf(tgChatId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
