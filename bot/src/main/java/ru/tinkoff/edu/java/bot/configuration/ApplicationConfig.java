package ru.tinkoff.edu.java.bot.configuration;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.reactive.function.client.WebClient;

@Validated
@ConfigurationProperties(prefix = "app", ignoreUnknownFields = false)
public record ApplicationConfig(@NotNull String test, String counterName) {
    @Bean("scrapperWebClient")
    public WebClient makeScrapperWebClient(@Value("${secrets.scrapper-base-url}") String scrapperBaseUrl) {
        return WebClient.create(scrapperBaseUrl);
    }

    @Bean
    public Counter messagesCounter(MeterRegistry meterRegistry) {
        return meterRegistry.counter(counterName);
    }
}
