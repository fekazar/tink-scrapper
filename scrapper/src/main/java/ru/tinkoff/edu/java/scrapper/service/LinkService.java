package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.records.LinkRecord;

import java.time.OffsetDateTime;
import java.util.List;

public interface LinkService {
    void add(String url, long chatId);
    void delete(String url, long chatId);
    List<LinkRecord> linksForChat(long chatId);
    List<LinkRecord> getAll();
    void updateLink(long linkId, LinkRecord newRecord);
    void setLastUpdate(String url, OffsetDateTime date);
}
