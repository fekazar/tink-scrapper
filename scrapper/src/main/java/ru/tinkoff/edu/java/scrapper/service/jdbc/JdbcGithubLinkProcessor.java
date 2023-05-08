package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;

@Slf4j
@AllArgsConstructor
public class JdbcGithubLinkProcessor implements LinkProcessor {
    public static final OffsetDateTime DEFAULT_LAST_UPDATE = OffsetDateTime.of(1954, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);


    public static final String HOST = "github.com";

    private final Parser linkParser;

    private final GithubClient githubClient;

    private final JdbcScrapperRepository scrapperRepository;


    @Override
    public LinkProcessor.Result process(Link linkRecord) {
        throw new UnsupportedOperationException("Github links processing is not implemented in Jdbc yet.");
    }
}
