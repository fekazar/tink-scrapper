package ru.tinkoff.edu.java.scrapper.repository;

// For the simplicity this repository is for the whole scrapper database

import io.swagger.v3.oas.models.links.Link;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

public interface ScrapperRepository {
    List<ChatRecord> getAllChats();
    List<LinkRecord> getAllLinks();
    List<LinkRecord> getLinksForChat(long chatId);

    boolean hasChat(long chatId);

    void addChat(long chatId);
    void addLink(String url, long chatId);

    void deleteChat(long chatId);
    void deleteLink(String url, long chatId);

    void updateLink(long id, LinkRecord newRecord);

    void setLastUpdate(String url, OffsetDateTime date);
}
