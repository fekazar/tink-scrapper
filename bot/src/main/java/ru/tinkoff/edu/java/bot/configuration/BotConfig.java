package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.tinkoff.edu.java.bot.tg.command.CommandProcessor;

// TODO: make preconfigured messaged centralized.

@Slf4j
@Configuration
@PropertySource("classpath:secrets.properties")
public class BotConfig {
    @Bean
    public String botApiKey(@Value("${secrets.bot_api_key}") String key) {
        return key;
    }

    @Bean
    public TelegramBot tgBot(@Qualifier("botApiKey") String key) {
        var bot = new TelegramBot(key);
        return bot;
    }
}
