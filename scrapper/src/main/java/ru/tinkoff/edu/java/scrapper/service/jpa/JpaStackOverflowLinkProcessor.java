package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;

@Slf4j
public class JpaStackOverflowLinkProcessor implements LinkProcessor {
    private StackOverflowClient stackOverflowClient;

    private Parser linkParser;

    private JpaLinkRepository linkRepository;

    public JpaStackOverflowLinkProcessor(StackOverflowClient stackOverflowClient, Parser linkParser, JpaLinkRepository linkRepository) {
        this.stackOverflowClient = stackOverflowClient;
        this.linkParser = linkParser;
        this.linkRepository = linkRepository;
    }

    @Override
    public Result process(Link linkRecord) {
        var sofLink = (StackoverflowLink) linkRecord;

        var parsedLink = (StackOverflowParser.Result) linkParser.parse(sofLink.toURL());

        var newQuestion = stackOverflowClient.getQuestions(parsedLink.id()).items().get(0);
        var newAnswers = stackOverflowClient.getAnswers(parsedLink.id()).getAnswers();

        log.info("Old answers: " + sofLink.getAnswers());
        log.info("New answers: " + newAnswers);

        var res = new Result();
        res.setLinkRecord(sofLink);

        var oldIter = sofLink.getAnswers().iterator();

        while (oldIter.hasNext()) {
            var old = oldIter.next();

            if (!newAnswers.contains(old)) {
                res.addUpdate(String.format("Answer '%s' was deleted.", old.getAnswerId())); // better set the answer Title
                res.setChanged();

                oldIter.remove();
                log.info("Deleted answer: " + old.getAnswerId());
            }
        }

        for (var newAns: newAnswers) {
            if (!sofLink.getAnswers().contains(newAns)) {
                res.addUpdate(String.format("There is a new answer: %s.", newAns.getAnswerId()));
                res.setChanged();

                sofLink.getAnswers().add(newAns);
                log.info("New answer: " + newAns.getAnswerId());
            }
        }

        if (!sofLink.getLastUpdate().isEqual(newQuestion.lastActivityDate())) {
            res.setChanged();
            res.addUpdate("There changes at question: " + parsedLink.id());
            log.info("Prev date: " + sofLink.getLastUpdate());

            sofLink.setLastUpdate(newQuestion.lastActivityDate());

            log.info("Changes at question: " + parsedLink.id());
        }

        linkRepository.save(sofLink);

        return res;
    }
}
