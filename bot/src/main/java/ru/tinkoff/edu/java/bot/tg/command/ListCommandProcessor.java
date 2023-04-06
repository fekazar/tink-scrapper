package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.stereotype.Component;

@Component("list")
public class ListCommandProcessor implements CommandProcessor {
    public static final String EMPTY_LIST = "List of tracking links is empty now. Try adding new via special command";
    public static final String LIST_COM = "list";

    @Override
    public String process(String command, String text) {
        return EMPTY_LIST;
    }
}
