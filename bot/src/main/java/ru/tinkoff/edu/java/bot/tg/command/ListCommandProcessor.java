package ru.tinkoff.edu.java.bot.tg.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

import java.util.ArrayList;
import java.util.List;

@Component(ListCommandProcessor.LIST_COM)
@Slf4j
public class ListCommandProcessor implements CommandProcessor {
    public static final String EMPTY_LIST = "List of tracking links is empty now. Try adding new via special command.";
    public static final String LIST_COM = "list";

    @Autowired
    private ScrapperClient scrapperClient;

    public ListCommandProcessor() {
    }

    public ListCommandProcessor(ScrapperClient scrapperClient) {
        this.scrapperClient = scrapperClient;
    }

    @Override
    public String process(String command, String text, long chatId) {
        try {
            var links = List.of(scrapperClient.getLinks(chatId));

            if (links.isEmpty())
                return EMPTY_LIST;

            var res = new StringBuilder();
            res.append("List of tracking links\n");

            for (int i = 0; i < links.size(); ++i)
                res.append(String.format("%d: %s\n", i + 1, links.get(i).url().toString()));

            return res.toString();
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Service unavailable.";
        }
    }
}
