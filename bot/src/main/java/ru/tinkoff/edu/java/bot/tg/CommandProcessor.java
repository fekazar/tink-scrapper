package ru.tinkoff.edu.java.bot.tg;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;

public interface CommandProcessor {
    SendMessage process(Message msg);
}
