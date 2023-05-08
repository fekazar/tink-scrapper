package ru.tinkoff.edu.java.scrapper.service.jdbc;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcStackAnswersRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.response.AnswersResponse;
import ru.tinkoff.edu.java.scrapper.response.StackOverflowResponse;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

// TODO: add url correctness check

@Slf4j
@AllArgsConstructor
public class JdbcLinkService implements LinkService {
    private final JdbcScrapperRepository repository;

    private final JdbcStackAnswersRepository stackAnswersRepository;

    private final StackOverflowClient stackOverflowClient;

    private final GithubClient githubClient;

    private final Map<String, LinkProcessor> linkProcessors;

    private final Parser linkParser;

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

                log.warn("Working with github links with jdbc. Some functionality is not implemented, may cause bugs.");
            } else if ("stackoverflow.com".equals(urlObj.getHost())) {
                var res = (StackOverflowParser.Result) linkParser.parse(urlObj);
                if (res == null)
                    throw new RuntimeException("Incorrect stackoverflow link " + url);

                AnswersResponse answersResponse = stackOverflowClient.getAnswers(res.id());
                StackOverflowResponse.Question stackQuestion = stackOverflowClient.getQuestions(res.id())
                    .items()
                    .get(0);

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
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(String url, long chatId) {
        repository.deleteLink(url, chatId);
    }

    @Override
    public void delete(long linkId) {
        throw new UnsupportedOperationException("Delete by id id not implemented in jdbc yet.");
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
                log.error("No pull requests are pulled for this link. Implementation will be later.");
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
    public LinkProcessor.Result process(Link link) {
        // find an appropriate processor
        // inject Map of processors from configuration
        return linkProcessors.get(link.toURL().getHost()).process(link);
    }
}
