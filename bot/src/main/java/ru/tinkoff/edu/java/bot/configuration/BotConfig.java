package ru.tinkoff.edu.java.bot.configuration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.tinkoff.edu.java.bot.tg.CommandProcessor;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

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

    // Are lambdas convenient enough? Shouldn't these implementations be in specific files?
    @Bean("track")
    public CommandProcessor trackProcessor() {
        return msg -> {
            var words = new ArrayList<String>(Arrays.asList(msg.text().split(" ")));
            int ind = words.indexOf("/track");

            if (ind == words.size() - 1) {
                return new SendMessage(msg.chat().id(), "Error: url is not specified.");
            }

            URL url = null;
            try {
                url = new URL(words.get(ind + 1));
            } catch (MalformedURLException e) {
                return new SendMessage(msg.chat().id(), "Error: url is incorrect.");
            }

            // TODO: send request to scrapper to start tracking this link.

            return new SendMessage(msg.chat().id(), "Tracking links is not supported for now.");
        };
    }

    @Bean("untrack")
    public CommandProcessor untrackProcessor() {
        return msg -> {
            // TODO: write a similar to track command logic
            return new SendMessage(msg.chat().id(), "Untracking links is unsupported for now.");
        };
    }

    @Bean("help")
    public CommandProcessor helpProcessor() {
        return msg -> {
            // TODO: return "help" messgage
            // make these messages centralized
            return new SendMessage(msg.chat().id(), "Welcome to the links tracking bot! Available commands are listed in Telegram.");
        };
    }

    @Bean("list")
    public CommandProcessor listProcessor() {
        return msg -> {
            return new SendMessage(msg.chat().id(), "Lists are empty for now.");
        };
    }
}
