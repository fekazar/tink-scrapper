package ru.tinkoff.edu.java.bot.tg.command;

// This interface assumes that the implementation can process
// given command.

// Should the process method receive chat id???

public interface CommandProcessor {

    String process(String command, String text, long chatId);
}
