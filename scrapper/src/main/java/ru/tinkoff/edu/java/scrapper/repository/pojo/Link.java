package ru.tinkoff.edu.java.scrapper.repository.pojo;

import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;

public class Link {
    @Setter
    protected long id;

    @Setter
    protected String url;

    @Setter
    protected long chatId;

    @Getter
    @Setter
    protected String hostType;

    protected OffsetDateTime lastUpdate;

    public Link() {
    }

    public Link(long id, String url, long chatId) {
        this.id = id;
        this.url = url;
        this.chatId = chatId;
    }

    public void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public long getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public long getChatId() {
        return chatId;
    }

    public OffsetDateTime getLastUpdate() {
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
