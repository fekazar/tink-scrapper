package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@AllArgsConstructor
public class JdbcGithubLinkProcessor implements LinkProcessor {
    public static OffsetDateTime DEFAULT_LAST_UPDATE = OffsetDateTime.of(1954, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    public static final String HOST = "github.com";

    private final Parser linkParser;

    private final GithubClient githubClient;

    private final JdbcScrapperRepository scrapperRepository;


    @Override
    public LinkProcessor.Result process(Link linkRecord) {
        throw new UnsupportedOperationException("Github links processing is not implemented in Jdbc yet.");
    }
}
