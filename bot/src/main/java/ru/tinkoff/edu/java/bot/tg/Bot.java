package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.MessageEntity;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class Bot implements AutoCloseable {
    private final TelegramBot bot;
    private final MessageProcessor messageProcessor;

    @Autowired
    public Bot(@Qualifier("tgBot") TelegramBot bot,
               MessageProcessor messageProcessor) {
        this.bot = bot;
        this.messageProcessor = messageProcessor;
    }
    @PostConstruct
    void init() {
        // TODO: make async processing for each request
        bot.setUpdatesListener(updates -> {
            for (var update: updates)
                processMessage(update);

            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

        log.info("Bot has been initialized.");
    }

    @Async("updatesExecutor")
    private void processMessage(@NotNull Update update) {
        log.info("Processing updates on: " + Thread.currentThread());

        if (update.message() == null)
            return;

        var msg = update.message();
        String answer = messageProcessor.process(msg);

        long chatId = msg.chat().id();
        bot.execute(new SendMessage(chatId, answer));
    }

    public void sendTextMessage(String msg, long tgChatId) {
        try {
            var toSend = new SendMessage(tgChatId, msg);
            bot.execute(toSend);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        bot.shutdown();
    }
}
