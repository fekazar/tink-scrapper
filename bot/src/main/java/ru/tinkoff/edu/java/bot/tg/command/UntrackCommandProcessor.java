package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

@Component(value = UntrackCommandProcessor.UNTRACK_COM)
public class UntrackCommandProcessor implements CommandProcessor {
    public static final String NO_URL = "Error: no URL is specified.";
    public static final String BAD_URL = "Error: given URL is incorrect.";
    public static final String UNTRACK_COM = "untrack";

    // This logic is similar to TrackCommandProcessor
    @Override
    public String process(String command, String text, long chatId) {
        var words = new ArrayList<String>(Arrays.asList(text.split(" ")));
        int ind = words.indexOf("/" + UNTRACK_COM);

        if (ind == words.size() - 1)
            return NO_URL;

        URL url = null;
        try {
            url = new URL(words.get(ind + 1));
        } catch (MalformedURLException e) {
            return BAD_URL;
        }

        // TODO: send request to scrapper to STOP tracking this link.

        return CommandProcessor.UNSUPPORTED_YET;
    }
}
