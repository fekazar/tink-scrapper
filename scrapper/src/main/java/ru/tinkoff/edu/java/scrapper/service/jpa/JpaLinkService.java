package ru.tinkoff.edu.java.scrapper.service.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@Service("jpaLinkService")
public class JpaLinkService implements LinkService {

    @Autowired
    private JpaLinkRepository linkRepository;
    @Override
    public void add(String url, long chatId) {
        String host = null;
        try {
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Link link = switch (host) {
            case "github.com" -> new GithubLink();
            case "stackoverflow.com" -> new StackoverflowLink();
            default -> new Link();
        };

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
