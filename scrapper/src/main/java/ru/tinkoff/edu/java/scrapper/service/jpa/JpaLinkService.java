package ru.tinkoff.edu.java.scrapper.service.jpa;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.tinkoff.edu.java.parser.GithubParser;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.parser.StackOverflowParser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.repository.pojo.GithubLink;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import ru.tinkoff.edu.java.scrapper.repository.pojo.StackoverflowLink;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

@Slf4j
public class JpaLinkService implements LinkService {

    private JpaLinkRepository linkRepository;

    private StackOverflowClient stackOverflowClient;

    private GithubClient githubClient;

    private Parser linkParser;

    private Map<String, LinkProcessor> linkProcessors;

    public JpaLinkService(JpaLinkRepository linkRepository, StackOverflowClient stackOverflowClient, GithubClient githubClient, Parser linkParser, Map<String, LinkProcessor> linkProcessors) {
        this.linkRepository = linkRepository;
        this.stackOverflowClient = stackOverflowClient;
        this.githubClient = githubClient;
        this.linkParser = linkParser;
        this.linkProcessors = linkProcessors;
    }

    @Override
    public void add(String url, long chatId) {
        String host = null;
        try {
            host = new URL(url).getHost();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Link link = new Link();

        if ("github.com".equals(host)) {
            link = new GithubLink();
            var githubParsed = (GithubParser.Result) linkParser.parse(link.toURL());
            var pullRequests = List.of(githubClient.getRepository(githubParsed.user(), githubParsed.repository()).getPulls());
            ((GithubLink) link).setPullRequests(pullRequests);

            log.info("Initial pull requests: " + pullRequests);
        } else if ("stackoverflow.com".equals(host)) {
            link = new StackoverflowLink();
            var sofParsed = (StackOverflowParser.Result) linkParser.parse(link.toURL());
            var answersResponse = stackOverflowClient.getAnswers(sofParsed.id());
            ((StackoverflowLink) link).setAnswers(answersResponse.getAnswers());

            log.info("Initial answers: " + answersResponse.getAnswers());
        }

        link.setChatId(chatId);
        link.setUrl(url);

        linkRepository.save(link);
    }

    @Override
    public void delete(String url, long chatId) {
        linkRepository.deleteByUrlAndChatId(url, chatId);
    }

    @Override
    public void delete(long linkId) {
        linkRepository.deleteById(linkId);
    }

    @Override
    public List<Link> linksForChat(long chatId) {
        return null;
    }

    @Override
    public List<Link> getAll() {
        return linkRepository.findAll();
    }

    @Override
    public LinkProcessor.Result process(Link link) {
        //throw new UnsupportedOperationException("Link processing is not implemented in jpa yet.");
        return linkProcessors.get(link.toURL().getHost()).process(link);
    }
}
