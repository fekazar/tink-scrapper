package ru.tinkoff.edu.java.scrapper.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
public class BotClient {
    private WebClient webClient;

    @PostConstruct
    private void init() {
        webClient = WebClient.builder()
                .baseUrl("http://localhost:8080") // TODO: load base url from properties file
                .build();
    }

    public void sendUpdates(RequestBody body) {
        try {
            webClient.post()
                    .uri(uriBuilder -> uriBuilder.pathSegment("updates").build())
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public record RequestBody(@JsonProperty("text_message") String textMessage,
                              String url,
                              @JsonProperty("tg_chat_id") long tgChatId) { }
}
