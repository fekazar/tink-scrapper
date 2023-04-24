package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcPullsRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;

import java.util.List;

@Slf4j
@Component("jpaGithubLinkProcessor")
public class JpaGithubLinkProcessor implements LinkProcessor {
    @Autowired
    @Qualifier("linkParser")
    private Parser linkParser;

    @Autowired
    private GithubClient githubClient;

    @Autowired
    private JpaLinkRepository linkRepository;

    @Override
    public Result process(Link linkRecord) {
        var ghLink = (GithubLink) linkRecord;

        GithubParser.Result parsed = (GithubParser.Result) linkParser.parse(ghLink.toURL());
        var repository = githubClient.getRepository(parsed.user(), parsed.repository());
        var newPullRequests = List.of(repository.getPulls());

        // TODO: check if changes are mapped to db

        var toReturn = new Result();
        toReturn.setLinkRecord(ghLink);

        log.info("Last update: " + ghLink.getLastUpdate());
        log.info("Pushed at: " + repository.pushedAt());

        if (!repository.pushedAt().isEqual(ghLink.getLastUpdate())) {
            toReturn.setChanged();

            toReturn.addUpdate("There are updates at github.");

            // TODO: realize why I return a link, even new...
            var newRec = new Link(ghLink.getId(), ghLink.getUrl(), ghLink.getChatId());
            newRec.setLastUpdate(repository.pushedAt());

            toReturn.setLinkRecord(newRec);
            ghLink.setLastUpdate(repository.pushedAt());
        } else {
            log.info("Times are equal: " + ghLink.getLastUpdate() + " " + repository.pushedAt());
        }

        log.info("Old prs: " + ghLink.getPullRequests());
        log.info("New prs: " + newPullRequests);

        // TODO: compare pull requests
        for (var oldPr: ghLink.getPullRequests()) {
            var newPrOptional = newPullRequests.stream().filter(pr -> pr.getId().equals(oldPr.getId())).findAny();
            if (newPrOptional.isEmpty()) {
                toReturn.addUpdate("Pull request was deleted: " + oldPr.getId());
                toReturn.setChanged();
            } else {
                var samePr = newPrOptional.get();
                if (!samePr.getState().equals(oldPr.getState())) {
                    log.info(String.format("Pull request %d changed state: %s -> %s.", oldPr.getId(), oldPr.getState(), samePr.getState()));
                    toReturn.addUpdate(String.format("Pull request %d changed state: %s -> %s.", oldPr.getId(), oldPr.getState(), samePr.getState()));
                    oldPr.setState(samePr.getState());
                    toReturn.setChanged();
                }
            }
        }

        for (var newPr: newPullRequests) {
            if (!ghLink.getPullRequests().contains(newPr)) {
                log.info("New pull request: " + newPr.getId());
                toReturn.addUpdate(String.format("New pull request %d", newPr.getId()));
                ghLink.getPullRequests().add(newPr);
                toReturn.setChanged();
            }
        }

        linkRepository.save(ghLink);
        return toReturn;
    }
}
