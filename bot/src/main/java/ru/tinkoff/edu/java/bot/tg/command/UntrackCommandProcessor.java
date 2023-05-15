package ru.tinkoff.edu.java.bot.tg.command;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

@Slf4j
@Component(value = UntrackCommandProcessor.UNTRACK_COM)
public class UntrackCommandProcessor implements CommandProcessor {
    public static final String NO_URL = "Error: no URL is specified.";
    public static final String BAD_URL = "Error: given URL is incorrect.";
    public static final String UNTRACK_COM = "untrack";

    @Autowired
    private ScrapperClient scrapperClient;

    // This logic is similar to TrackCommandProcessor
    @Override
    public String process(String command, String text, long chatId) {
        List<String> words = new ArrayList<String>(Arrays.asList(text.split("\\s")))
                .stream().filter(s -> !s.isEmpty()).toList();

        int ind = words.indexOf("/" + UNTRACK_COM);

        if (ind == words.size() - 1)
            return NO_URL;

        URL url = null;
        try {
            url = new URL(words.get(ind + 1));
            scrapperClient.deleteLink(chatId, url);
            return url + " is not tracking anymore.";
        } catch (MalformedURLException e) {
            log.error(e.getMessage());
            return BAD_URL;
        }
    }
}
