package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.util.List;

@Component
public class JpaLinkService implements LinkService {

    @Autowired
    private JpaLinkRepository linkRepository;
    @Override
    public void add(String url, long chatId) {
        var link = new Link();
        link.setChatId(chatId);
        link.setUrl(url);
        linkRepository.save(link);
    }

    @Override
    public void delete(String url, long chatId) {
        linkRepository.deleteByUrlAndChatId(url, chatId);
    }

    @Override
    public void delete(long linkId) {
        linkRepository.deleteById(linkId);
    }

    @Override
    public List<Link> linksForChat(long chatId) {
        return null;
    }

    @Override
    public List<Link> getAll() {
        return linkRepository.findAll();
    }

    @Override
    public LinkProcessor.Result process(Link link) {
        throw new UnsupportedOperationException("Link processing is not implemented in jpa yet.");
        //return null;
    }
}
