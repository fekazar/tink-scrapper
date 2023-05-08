package ru.tinkoff.edu.java.bot.tg.command;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.client.ScrapperClient;

@Slf4j
@Component(value = TrackCommandProcessor.TRACK_COM)
public class TrackCommandProcessor implements CommandProcessor {
    public static final String NO_URL = "Error: no URL is specified.";
    public static final String BAD_URL = "Error: given URL is incorrect.";
    public static final String TRACK_COM = "track";

    @Autowired
    private ScrapperClient scrapperClient;

    @Override
    public String process(String command, String text, long chatId) {
        List<String> words = new ArrayList<String>(Arrays.asList(text.split("\\s")))
                .stream().filter(s -> !s.isEmpty()).toList();

        int ind = words.indexOf("/" + TRACK_COM);

        if (ind == words.size() - 1)
            return NO_URL;

        URL url = null;
        try {
            url = new URL(words.get(ind + 1));
            var res = scrapperClient.addLink(url, chatId);
            return "Link is tracking now: " + url;
        } catch (Exception e) {
            log.error(e.getMessage());
            return BAD_URL;
        }
    }
}
