package ru.tinkoff.edu.java.scrapper.service.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

// TODO: add url correctness check

@Service("jdbcLinkService")
public class JdbcLinkService implements LinkService {
    @Autowired
    private JdbcScrapperRepository repository;

    public void add(String url, long chatId) {
        repository.addLink(url, chatId);
    }

    public void delete(String url, long chatId) {
        repository.deleteLink(url, chatId);
    }

    public List<LinkRecord> linksForChat(long chatId) {
        return repository.getLinksForChat(chatId);
    }

    public List<LinkRecord> getAll() {
        return repository.getAllLinks();
    }

    @Override
    public void updateLink(long linkId, LinkRecord newRecord) {
        repository.updateLink(linkId, newRecord);
    }

    @Override
    public void setLastUpdate(String url, OffsetDateTime date) {
        repository.setLastUpdate(url, date);
    }
}
