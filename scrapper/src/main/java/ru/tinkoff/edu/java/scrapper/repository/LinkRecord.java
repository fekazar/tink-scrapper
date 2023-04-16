package ru.tinkoff.edu.java.scrapper.repository;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;

public class LinkRecord {
    private final long id;
    private final String url;
    private final long chatId;

    private OffsetDateTime lastUpdate;

    public LinkRecord(long id, String url, long chatId) {
        this.id = id;
        this.url = url;
        this.chatId = chatId;
    }

    public void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long id() {
        return id;
    }

    public String url() {
        return url;
    }

    public long chatId() {
        return chatId;
    }

    public OffsetDateTime lastUpdate() {
        return lastUpdate;
    }

    public URL toURL() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }


}
