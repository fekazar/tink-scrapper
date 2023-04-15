package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

import java.util.List;

public interface LinkService {
    void add(String url, int chatId);
    void delete(String url, int chatId);
    List<LinkRecord> linksForChat(int chatId);
    List<LinkRecord> getAll();
}
