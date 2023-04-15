package ru.tinkoff.edu.java.scrapper.repository;

// For the simplicity this repository is for the whole scrapper database

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
}
