package ru.tinkoff.edu.java.scrapper.service.jdbc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcStackAnswersRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcPullsRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.response.AnswersResponse;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

// TODO: add url correctness check

@Service("jdbcLinkService")
@Slf4j
public class JdbcLinkService implements LinkService {
    @Autowired
    private JdbcScrapperRepository repository;

    @Autowired
    private JdbcPullsRepository pullsRepository;

    @Autowired
    private JdbcStackAnswersRepository stackAnswersRepository;

    @Autowired
    private StackOverflowClient stackOverflowClient;

    @Autowired
    private GithubClient githubClient;

    @Autowired
    @Qualifier("linkParser")
    private Parser linkParser;

    public void add(String url, long chatId) {
        try {
            var urlObj = new URL(url);

            if ("github.com".equals(urlObj.getHost())) {
                var parsedUrl = (GithubParser.Result) linkParser.parse(urlObj);

                var newLinkRecord = new Link();
                var githubRepo = githubClient.getRepository(parsedUrl.user(), parsedUrl.repository());

                newLinkRecord.setUrl(url);
                newLinkRecord.setChatId(chatId);
                newLinkRecord.setLastUpdate(githubRepo.pushedAt());

                repository.addTypedLink(newLinkRecord, "github");
            } else if ("stackoverflow.com".equals(urlObj.getHost())) {
                var res = (StackOverflowParser.Result) linkParser.parse(urlObj);
                if (res == null)
                    throw new RuntimeException("Incorrect stackoverflow link " + url);

                AnswersResponse answersResponse = stackOverflowClient.getAnswers(res.id());
                StackOverflowResponse.Question stackQuestion = stackOverflowClient.getQuestions(res.id()).items().get(0); // shouldn't be null

                var newLinkRecord = new Link();

                newLinkRecord.setUrl(url);
                newLinkRecord.setChatId(chatId);
                newLinkRecord.setLastUpdate(stackQuestion.lastActivityDate());

                // may cause some problems, if the scheduler process this link, but default values aren't set
                repository.addTypedLink(newLinkRecord, "stackoverflow");
                long linkId = repository.getLinkId(url);

                for (var answer: answersResponse.getAnswers())
                    stackAnswersRepository.addAnswer(linkId, answer);
            } else {
                repository.addLink(url, chatId);
            }

            pullsRepository.createNewPullString(url);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String url, long chatId) {
        repository.deleteLink(url, chatId);
    }

    public List<Link> linksForChat(long chatId) {
        return getAll()
                .stream()
                .filter(linkRecord -> linkRecord.getChatId() == chatId)
                .collect(Collectors.toList());
    }

    public List<Link> getAll() {
        List<Link> records = repository.getAllLinks();

        for (Link rec: records) {

            if (rec instanceof GithubLink ghRec) {
                // set pulls string for this record
                try {
                    ghRec.setPullsString(pullsRepository.pullsStringForUrl(rec.getUrl()));
                } catch (Exception e) {
                    log.error("Error in requesting pull string for: " + rec.getUrl());
                    log.error(e.getMessage());
                }
            } else if (rec instanceof StackoverflowLink stackRec) {
                try {
                    stackRec.setAnswers(stackAnswersRepository.getAnswersFor(stackRec.getId()));
                } catch (Exception e) {
                    log.error("Error in requesting answers for: " + stackRec.getUrl());
                    log.error(e.getMessage());
                }
            }
        }

        return records;
    }

    @Override
    public void updateLink(long linkId, Link newRecord) {
        repository.updateLink(linkId, newRecord);
    }

    @Override
    public void setLastUpdate(String url, OffsetDateTime date) {
        repository.setLastUpdate(url, date);
    }
}
