package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    void add(String url, long chatId);
    void delete(String url, long chatId);
    List<Link> linksForChat(long chatId);
    List<Link> getAll();
    void updateLink(long linkId, Link newRecord);
    void setLastUpdate(String url, OffsetDateTime date);
}
