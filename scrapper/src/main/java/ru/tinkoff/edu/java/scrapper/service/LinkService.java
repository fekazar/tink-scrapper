package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

import java.util.List;

public interface LinkService {
    void add(String url, long chatId);
    void delete(String url, long chatId);
    List<LinkRecord> linksForChat(long chatId);
    List<LinkRecord> getAll();
}
