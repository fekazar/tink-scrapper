package ru.tinkoff.edu.java.scrapper.client.urlprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Component(GithubUrlProcessor.HOST)
public class GithubUrlProcessor implements UrlProcessor {
    public static OffsetDateTime DEFAULT_LAST_UPDATE = OffsetDateTime.of(1954, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    public static final String HOST = "github.com";

    @Autowired
    private Parser linkParser;

    @Autowired
    private GithubClient githubClient;

    @Override
    public UrlProcessor.Result process(LinkRecord linkRecord) {
        GithubParser.Result parsed = (GithubParser.Result) linkParser.parse(linkRecord.toURL());
        var res = githubClient.getRepository(parsed.user(), parsed.repository()).block();

        if (res.pushedAt().isEqual(linkRecord.lastUpdate()) && !linkRecord.lastUpdate().isEqual(DEFAULT_LAST_UPDATE))
            return new Result(linkRecord, "No changes at github");

        log.info(res.pushedAt() + " - " + linkRecord.lastUpdate());
        log.info(res.pushedAt().toLocalDateTime() + " - " + linkRecord.lastUpdate().toLocalDateTime());

        var toReturn = new LinkRecord(linkRecord.id(), linkRecord.url(), linkRecord.chatId());
        toReturn.setLastUpdate(res.pushedAt());

        return new Result(toReturn, "There are some changes at github."); // build more verbose description here
    }
}
