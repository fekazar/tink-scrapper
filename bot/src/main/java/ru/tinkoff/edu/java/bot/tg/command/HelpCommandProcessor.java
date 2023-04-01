package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.stereotype.Component;

@Component("help")
public class HelpCommandProcessor implements CommandProcessor {
    public static final String HELP_MSG = "Welcome to the links tracking bot! Available commands are listed in Telegram.";

    @Override
    public String process(String command, String text) {
        // TODO: return "help" messgage
        // make these messages centralized
        return HELP_MSG;
    }
}
