package ru.tinkoff.edu.java.scrapper.repository;

import java.net.MalformedURLException;
import java.net.URL;

public record LinkRecord(int id, String url, int chatId) {
    public URL toURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}
