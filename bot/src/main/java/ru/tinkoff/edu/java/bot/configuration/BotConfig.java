package ru.tinkoff.edu.java.bot.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:secrets.properties")
public class BotConfig {
    @Bean
    public String botApiKey(@Value("${secrets.bot_api_key}") String key) {
        return key;
    }
}
