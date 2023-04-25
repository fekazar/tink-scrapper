package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcStackAnswersRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;

@Slf4j
public class JdbcStackOverflowLinkProcessor implements LinkProcessor {
    public static final String HOST = "stackoverflow.com";

    private Parser linkParser;

    private StackOverflowClient stackOverflowClient;

    private JdbcStackAnswersRepository answersRepository;

    private BotClient botClient;

    private JdbcScrapperRepository scrapperRepository;

    public JdbcStackOverflowLinkProcessor(Parser linkParser,
                                          StackOverflowClient stackOverflowClient,
                                          JdbcStackAnswersRepository answersRepository,
                                          BotClient botClient,
                                          JdbcScrapperRepository scrapperRepository) {
        this.linkParser = linkParser;
        this.stackOverflowClient = stackOverflowClient;
        this.answersRepository = answersRepository;
        this.botClient = botClient;
        this.scrapperRepository = scrapperRepository;
    }

    @Override
    public Result process(Link linkRecord) {
        var sofLink = (StackoverflowLink) linkRecord;

        var parsedLink = (StackOverflowParser.Result) linkParser.parse(sofLink.toURL());

        var newQuestion = stackOverflowClient.getQuestions(parsedLink.id()).items().get(0);
        var newAnswers = stackOverflowClient.getAnswers(parsedLink.id()).getAnswers();

        var res = new Result();
        res.setLinkRecord(sofLink);

        var oldIter = sofLink.getAnswers().iterator();

        while (oldIter.hasNext()) {
            var old = oldIter.next();

            if (!newAnswers.contains(old)) {
                res.addUpdate(String.format("Answer '%s' was deleted.", old.getAnswerId())); // better set the answer Title
                res.setChanged();

                answersRepository.deleteAnswer(old.getAnswerId());
                oldIter.remove();
                log.info("Deleted answer: " + old.getAnswerId());
            }
        }

        for (var newAns: newAnswers) {
            if (!sofLink.getAnswers().contains(newAns)) {
                res.addUpdate(String.format("There is a new answer: %s.", newAns.getAnswerId()));
                res.setChanged();

                answersRepository.addAnswer(sofLink.getId(), newAns);
                sofLink.getAnswers().add(newAns);
                log.info("New answer: " + newAns.getAnswerId());
            }
        }

        if (!sofLink.getLastUpdate().isEqual(newQuestion.lastActivityDate())) {
            res.setChanged();
            res.addUpdate("There changes at question: " + parsedLink.id());
            log.info("Prev date: " + sofLink.getLastUpdate());

            sofLink.setLastUpdate(newQuestion.lastActivityDate());

            scrapperRepository.updateLink(sofLink.getId(), sofLink);
            log.info("Changes at question: " + parsedLink.id());
        }

        return res;
    }
}