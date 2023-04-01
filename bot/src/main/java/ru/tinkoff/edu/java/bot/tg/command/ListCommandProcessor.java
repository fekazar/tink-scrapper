package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.stereotype.Component;

@Component("list")
public class ListCommandProcessor implements CommandProcessor {
    public static final String EMPTY_LIST = "List of tracking links is empty now. Try adding new via special command";

    @Override
    public String process(String command, String text) {
        return EMPTY_LIST;
    }
}
