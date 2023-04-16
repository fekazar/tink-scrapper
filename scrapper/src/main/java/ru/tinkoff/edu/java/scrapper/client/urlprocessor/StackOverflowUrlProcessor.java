package ru.tinkoff.edu.java.scrapper.client.urlprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.BotClient;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.LinkRecord;

import java.net.URL;

@Component(StackOverflowUrlProcessor.HOST)
@Slf4j
public class StackOverflowUrlProcessor implements UrlProcessor {
    public static final String HOST = "stackoverflow.com";

    @Autowired
    private Parser linkParser;

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Autowired
    private BotClient botClient;

    @Override
    public Result process(LinkRecord linkRecord) {
        var parsed = (StackOverflowParser.Result) linkParser.parse(linkRecord.toURL());
        var updates = stackOverflowClient.getQuestions(parsed.id()).block();

        log.info(updates.items().get(0).lastActivityDate() + " - " + linkRecord.lastUpdate());
        log.info(updates.items().get(0).lastActivityDate().toLocalDate() + " - " + linkRecord.lastUpdate().toLocalDate());

        if (updates.items().get(0).lastActivityDate().isEqual(linkRecord.lastUpdate()))
            return new Result(linkRecord, "No changes at stackoverflow");

        var toReturn = new LinkRecord(linkRecord.id(), linkRecord.url(), linkRecord.chatId());
        toReturn.setLastUpdate(updates.items().get(0).lastActivityDate());

        return new Result(toReturn, "There are new changes at stackoverflow"); // build more verbouse message
    }
}
