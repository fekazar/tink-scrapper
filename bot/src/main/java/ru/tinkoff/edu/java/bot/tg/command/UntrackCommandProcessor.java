package ru.tinkoff.edu.java.bot.tg.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component("untrack")
public class UntrackCommandProcessor implements CommandProcessor {
    @Override
    public SendMessage process(Message msg) {
        // TODO: write a similar to track command logic
        return new SendMessage(msg.chat().id(), "Untracking links is unsupported for now.");
    }
}
