package ru.tinkoff.edu.java.scrapper.service.jpa;

import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;

public class JpaLinkService implements LinkService {
    @Override
    public void add(String url, long chatId) {

    }

    @Override
    public void delete(String url, long chatId) {

    }

    @Override
    public List<Link> linksForChat(long chatId) {
        return null;
    }

    @Override
    public List<Link> getAll() {
        return null;
    }

    @Override
    public LinkProcessor.Result process(Link link) {
        return null;
    }
}
