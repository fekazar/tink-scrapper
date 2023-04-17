package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.scrapper.repository.GithubLinkRecord;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcPullsRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.List;

// TODO: add url correctness check

@Service("jdbcLinkService")
@Slf4j
public class JdbcLinkService implements LinkService {
    @Autowired
    private JdbcScrapperRepository repository;

    @Autowired
    private JdbcPullsRepository pullsRepository;

    public void add(String url, long chatId) {

        try {
            var urlObj = new URL(url);
            if ("github.com".equals(urlObj.getHost()))
                repository.addTypedLink(url, chatId, "github");
            else
                repository.addLink(url, chatId);

            pullsRepository.createNewPullString(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String url, long chatId) {
        repository.deleteLink(url, chatId);
    }

    public List<LinkRecord> linksForChat(long chatId) {
        return repository.getLinksForChat(chatId);
    }

    public List<LinkRecord> getAll() {
        List<LinkRecord> records = repository.getAllLinks();

        for (LinkRecord rec: records) {
            if (rec instanceof GithubLinkRecord) {
                // set pulls string for this record
                try {
                    ((GithubLinkRecord) rec).setPullsString(pullsRepository.pullsStringForUrl(rec.url()));
                } catch (Exception e) {
                    log.error("Error in requesting pull string for: " + rec.url());
                    log.error(e.getMessage());
                }
            }
        }

        return records;
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
