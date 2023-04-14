package ru.tinkoff.edu.java.scrapper.repository;

// For the simplicity this repository is for the whole scrapper database

import java.util.List;

public interface ScrapperRepository {
    List<ChatRecord> getAllChats();
    List<LinkRecord> getAllLinks();
    List<LinkRecord> getLinksForChat(int chatId);

    void addChat(int chatId);
    void addLink(String url, int chatId);

    void deleteChat(int chatId);
    void deleteLink(String url, int chatId);
}
