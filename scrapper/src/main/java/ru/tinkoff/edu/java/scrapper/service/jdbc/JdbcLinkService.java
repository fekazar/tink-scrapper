package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

import java.util.List;

@Service
public class JdbcLinkService {
    @Autowired
    private JdbcScrapperRepository repository;

    public void add(String url, int chatId) {
        repository.addLink(url, chatId);
    }

    public void delete(String url, int chatId) {
        repository.deleteLink(url, chatId);
    }

    public List<LinkRecord> linksForChat(int chatId) {
        return repository.getLinksForChat(chatId);
    }

    public List<LinkRecord> getAll() {
        return repository.getAllLinks();
    }
}
