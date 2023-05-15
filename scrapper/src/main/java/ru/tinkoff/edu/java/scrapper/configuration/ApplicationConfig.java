package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.validation.annotation.Validated;

@Validated
@EnableScheduling
@ConfigurationProperties(prefix = "app")
public record ApplicationConfig(@NotNull String test,
                                Scheduler scheduler) {
    @Bean
    long getRateMillis() {
        return scheduler.interval().toMillis();
    }

    public record Scheduler(Duration interval) {}
}
