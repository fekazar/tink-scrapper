package ru.tinkoff.edu.java.scrapper.service;

import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;

import java.util.List;

public interface LinkService {
    void add(String url, long chatId);
    void delete(String url, long chatId);
    List<Link> linksForChat(long chatId);
    List<Link> getAll();

    LinkProcessor.Result process(Link link);
}
