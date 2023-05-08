package ru.tinkoff.edu.java.bot.client;

import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.tinkoff.edu.java.bot.client.dto.AddLinkRequest;
import ru.tinkoff.edu.java.bot.client.dto.LinkResponse;
import ru.tinkoff.edu.java.bot.client.dto.RemoveLinkRequest;

@Component
public class ScrapperClient {
    public static final String LINKS = "links";
    public static final String TG_ID_HEADER = "tgChatId";

    @Autowired
    @Qualifier("scrapperWebClient")
    private final WebClient client;

    public ScrapperClient(WebClient client) {
        this.client = client;
    }

    public LinkResponse addLink(URL url, long chatId) {
        var request = new AddLinkRequest(url);

        return client.post()
                .uri(uriBuilder -> uriBuilder.pathSegment(LINKS).build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(TG_ID_HEADER, String.valueOf(chatId))
                .bodyValue(request)
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    public LinkResponse deleteLink(long tgChatId, URL url) {
        return client.method(HttpMethod.DELETE)
                .uri(uriBuilder -> uriBuilder.pathSegment(LINKS).build())
                .header(TG_ID_HEADER, String.valueOf(tgChatId))
                .bodyValue(new RemoveLinkRequest(url))
                .retrieve()
                .bodyToMono(LinkResponse.class)
                .block();
    }

    public LinkResponse[] getLinks(long tgChatId) {
        return client.get()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(LINKS)
                        .build())
                .header(TG_ID_HEADER, String.valueOf(tgChatId))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(LinkResponse[].class)
                .block();
    }

    public void registerChat(long tgChatId) {
        client.post()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment("tg-chat")
                        .pathSegment(String.valueOf(tgChatId)).build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public void deleteChat(long tgChatId) {
        client.delete()
                .uri(uriBuilder -> uriBuilder
                        .pathSegment(LINKS)
                        .pathSegment(String.valueOf(tgChatId)).build())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(TG_ID_HEADER, String.valueOf(tgChatId))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
