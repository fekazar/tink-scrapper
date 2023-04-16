package ru.tinkoff.edu.java.scrapper.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Slf4j
@PropertySource("classpath:secrets.properties")
public class BotClient {
    private WebClient webClient;

    @Value("${secrets.bot-base-url}")
    private String botBaseUrl;

    @PostConstruct
    private void init() {
        webClient = WebClient.builder()
                .baseUrl(botBaseUrl)
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
