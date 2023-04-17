package ru.tinkoff.edu.java.scrapper.client.urlprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.repository.GithubLinkRecord;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcPullsRepository;
import ru.tinkoff.edu.java.scrapper.response.PullsResponse;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.stream.Collectors;

@Slf4j
@Component(GithubUrlProcessor.HOST)
public class GithubUrlProcessor implements UrlProcessor {
    public static OffsetDateTime DEFAULT_LAST_UPDATE = OffsetDateTime.of(1954, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);

    public static final String HOST = "github.com";

    @Autowired
    private Parser linkParser;

    @Autowired
    private GithubClient githubClient;

    @Autowired
    private JdbcPullsRepository pullsRepository;

    @Autowired
    private JdbcLinkService linkService;

    @Override
    public UrlProcessor.Result process(LinkRecord linkRecord) {
        GithubParser.Result parsed = (GithubParser.Result) linkParser.parse(linkRecord.toURL());
        var res = githubClient.getRepository(parsed.user(), parsed.repository());

        var toReturn = new Result();
        toReturn.setLinkRecord(linkRecord);

        // res.pulls shouldn't be null
        log.info("Pulls number: " + res.getPulls().length);
        for (PullsResponse pull: res.getPulls())
            log.info(String.valueOf(pull));

        log.info("Last update: " + linkRecord.lastUpdate());
        log.info("Pushed at: " + res.pushedAt());

        if (linkRecord.lastUpdate().isEqual(DEFAULT_LAST_UPDATE)) {
            log.info("Link had default update: " + linkRecord.url());
            linkService.setLastUpdate(linkRecord.url(), res.pushedAt());
        } else if (!res.pushedAt().isEqual(linkRecord.lastUpdate())) {
            toReturn.setChanged();

            toReturn.addUpdate("There is a new push at github.");

            var newRec = new LinkRecord(linkRecord.id(), linkRecord.url(), linkRecord.chatId());
            newRec.setLastUpdate(res.pushedAt());

            toReturn.setLinkRecord(newRec);
            linkService.setLastUpdate(newRec.url(), newRec.lastUpdate());
        } else {
            log.info("Times are equal: " + linkRecord.lastUpdate() + " " + res.pushedAt());
        }

        var oldPulls = ((GithubLinkRecord) linkRecord).getPullsString();
        var newPulls = buildBinaryString(res.getPulls());

        if (!newPulls.equals(oldPulls)) {
            if (!oldPulls.isEmpty()) {
                toReturn.setChanged();
                for (int i = 0; i < Math.min(newPulls.length(), oldPulls.length()); ++i) {
                    if (newPulls.charAt(i) != oldPulls.charAt(i)) {
                        toReturn.addUpdate(String.format("PR #%d was closed.", i + 1));
                    }
                }

                toReturn.addUpdate(String.format("There are %d new PR-s.", newPulls.length() - oldPulls.length()));
            }

            // Whose responsibility to save new pulls string to database???
            pullsRepository.updatePulls(linkRecord.url(), newPulls);
        }

        return toReturn;
    }

    private static String buildBinaryString(PullsResponse[] pulls) {
        return Arrays.stream(pulls)
                .map(pullsResponse -> "open".equals(pullsResponse.getState()) ? "1" : "0")
                .collect(Collectors.joining(""));
    }
}
