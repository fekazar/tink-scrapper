package ru.tinkoff.edu.java.scrapper.client.urlprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcStackAnswersRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

@Component(StackOverflowUrlProcessor.HOST)
@Slf4j
public class StackOverflowUrlProcessor implements UrlProcessor {
    public static final String HOST = "stackoverflow.com";

    @Autowired
    private Parser linkParser;

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Autowired
    private JdbcStackAnswersRepository answersRepository;

    @Autowired
    private LinkService linkService;

    @Autowired
    private BotClient botClient;

    @Override
    public Result process(Link linkRecord) {
        var parsedLink = (StackOverflowParser.Result) linkParser.parse(linkRecord.toURL());

        var newQuestion = stackOverflowClient.getQuestions(parsedLink.id()).items().get(0);
        var oldAnswers = answersRepository.getAnswersFor(linkRecord.getId());
        var newAnswers = stackOverflowClient.getAnswers(parsedLink.id()).getAnswers();

        var res = new Result();
        res.setLinkRecord(linkRecord);

        for (var old: oldAnswers) {
            if (!newAnswers.contains(old)) {
                res.addUpdate(String.format("Answer '%s' was deleted.", old.getAnswerId())); // better set the answer Title
                res.setChanged();

                answersRepository.deleteAnswer(old.getAnswerId());
                log.info("Deleted answer: " + old.getAnswerId());
            }
        }

        for (var newAns: newAnswers) {
            if (!oldAnswers.contains(newAns)) {
                res.addUpdate(String.format("There is a new answer: %s.", newAns.getAnswerId()));
                res.setChanged();

                answersRepository.addAnswer(linkRecord.getId(), newAns);
                log.info("New answer: " + newAns.getAnswerId());
            }
        }

        if (!linkRecord.getLastUpdate().isEqual(newQuestion.lastActivityDate())) {
            res.setChanged();
            res.addUpdate("There changes at question: " + parsedLink.id());
            log.info("Prev date: " + linkRecord.getLastUpdate());

            linkRecord.setLastUpdate(newQuestion.lastActivityDate());

            linkService.updateLink(linkRecord.getId(), linkRecord);
            log.info("Changes at question: " + parsedLink.id());
        }

        return res;
    }
}
