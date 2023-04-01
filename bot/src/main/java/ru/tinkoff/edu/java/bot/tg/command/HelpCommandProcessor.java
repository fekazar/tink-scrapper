package ru.tinkoff.edu.java.bot.tg.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

@Component("help")
public class HelpCommandProcessor implements CommandProcessor {
    @Override
    public SendMessage process(Message msg) {
        // TODO: return "help" messgage
        // make these messages centralized
        return new SendMessage(msg.chat().id(), "Welcome to the links tracking bot! Available commands are listed in Telegram.");
    }
}
