package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

import java.util.ArrayList;
import java.util.List;

@Component(ListCommandProcessor.LIST_COM)
public class ListCommandProcessor implements CommandProcessor {
    public static final String EMPTY_LIST = "List of tracking links is empty now. Try adding new via special command";
    public static final String LIST_COM = "list";

    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String process(String command, String text, long chatId) {
        try {
            return List.of(scrapperClient.getLinks(chatId)).toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "erorr";
        }
    }
}
