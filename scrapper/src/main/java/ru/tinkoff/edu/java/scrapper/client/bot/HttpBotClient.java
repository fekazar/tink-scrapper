package ru.tinkoff.edu.java.scrapper.client.bot;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@PropertySource("classpath:secrets.properties")
public class HttpBotClient implements BotClient {
    private WebClient webClient;

    public HttpBotClient(String botBaseUrl) {
        this.botBaseUrl = botBaseUrl;
    }

    private String botBaseUrl;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl(botBaseUrl)
                .build();
    }

    @Override
    public void sendUpdates(LinkUpdate body) {
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
}
