package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcPullsRepository;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JdbcGithubLinkProcessor implements LinkProcessor {
    public static OffsetDateTime DEFAULT_LAST_UPDATE = OffsetDateTime.of(1954, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    public static final String HOST = "github.com";

    @Autowired
    @Qualifier("linkParser")
    private Parser linkParser;

    @Autowired
    private GithubClient githubClient;

    @Autowired
    private JdbcPullsRepository pullsRepository;

    @Autowired
    private JdbcScrapperRepository scrapperRepository;


    @Override
    public LinkProcessor.Result process(Link linkRecord) {
        GithubParser.Result parsed = (GithubParser.Result) linkParser.parse(linkRecord.toURL());
        var repository = githubClient.getRepository(parsed.user(), parsed.repository());

        var toReturn = new Result();
        toReturn.setLinkRecord(linkRecord);

        log.info("Last update: " + linkRecord.getLastUpdate());
        log.info("Pushed at: " + repository.pushedAt());

        if (!repository.pushedAt().isEqual(linkRecord.getLastUpdate())) {
            toReturn.setChanged();

            toReturn.addUpdate("There is a new push at github.");

            var newRec = new Link(linkRecord.getId(), linkRecord.getUrl(), linkRecord.getChatId());
            newRec.setLastUpdate(repository.pushedAt());

            toReturn.setLinkRecord(newRec);
            //linkService.setLastUpdate(newRec.getUrl(), newRec.getLastUpdate());
            scrapperRepository.setLastUpdate(newRec.getUrl(), newRec.getLastUpdate());

        } else {
            log.info("Times are equal: " + linkRecord.getLastUpdate() + " " + repository.pushedAt());
        }

        return toReturn;
    }
}
