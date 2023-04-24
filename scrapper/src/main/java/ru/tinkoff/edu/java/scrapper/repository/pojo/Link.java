package ru.tinkoff.edu.java.scrapper.repository.pojo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;

@Entity(name = "links")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "host_type")
@Getter
@Setter
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected long id;

    protected String url;

    @Column(name = "chat_id")
    protected long chatId;

    @Column(name = "host_type", insertable = false, updatable = false)
    protected String hostType;

    @Column(name = "last_update", insertable = false, updatable = false)
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
