package ru.tinkoff.edu.java.scrapper.repository;

// For the simplicity this repository is for the whole scrapper database

import ru.tinkoff.edu.java.scrapper.repository.pojo.Chat;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface ScrapperRepository {
    List<Chat> getAllChats();
    List<Link> getAllLinks();
    List<Link> getLinksForChat(long chatId);

    boolean hasChat(long chatId);

    void addChat(long chatId);
    void addLink(String url, long chatId);

    void deleteChat(long chatId);
    void deleteLink(String url, long chatId);

    void updateLink(long id, Link newRecord);

    void setLastUpdate(String url, OffsetDateTime date);
}
