package ru.tinkoff.edu.java.bot.tg.command;

import org.springframework.stereotype.Component;

import javax.sound.midi.Track;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

@Component(value = TrackCommandProcessor.TRACK_COM)
public class TrackCommandProcessor implements CommandProcessor {
    public static final String NO_URL = "Error: no URL is specified.";
    public static final String BAD_URL = "Error: given URL is incorrect.";
    public static final String TRACK_COM = "track";

    @Override
    public String process(String command, String text) {
        var words = new ArrayList<String>(Arrays.asList(text.split(" ")));
        int ind = words.indexOf("/" + TRACK_COM);

        if (ind == words.size() - 1)
            return NO_URL;

        URL url = null;
        try {
            url = new URL(words.get(ind + 1));
        } catch (MalformedURLException e) {
            return BAD_URL;
        }

        // TODO: send request to scrapper to START tracking this link.

        return CommandProcessor.UNSUPPORTED_YET;
    }
}