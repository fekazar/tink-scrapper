package ru.tinkoff.edu.java.bot.tg.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component("list")
public class ListCommandProcessor implements CommandProcessor {
    @Override
    public SendMessage process(Message msg) {
        return new SendMessage(msg.chat().id(), "Lists are empty for now.");
    }
}
