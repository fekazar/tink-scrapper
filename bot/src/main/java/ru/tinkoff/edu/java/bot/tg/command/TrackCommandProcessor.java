package ru.tinkoff.edu.java.bot.tg.command;

import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

@Component("track")
public class TrackCommandProcessor implements CommandProcessor {
    @Override
    public SendMessage process(Message msg) {
        var words = new ArrayList<String>(Arrays.asList(msg.text().split(" ")));
        int ind = words.indexOf("/track");

        if (ind == words.size() - 1) {
            return new SendMessage(msg.chat().id(), "Error: url is not specified.");
        }

        URL url = null;
        try {
            url = new URL(words.get(ind + 1));
        } catch (MalformedURLException e) {
            return new SendMessage(msg.chat().id(), "Error: url is incorrect.");
        }

        // TODO: send request to scrapper to start tracking this link.

        return new SendMessage(msg.chat().id(), "Tracking links is not supported for now.");
    }
}
