package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;
import ru.tinkoff.edu.java.scrapper.client.bot.HttpBotClient;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.ChatRowMapper;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcStackAnswersRepository;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.LinksRowMapper;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcGithubLinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcStackOverflowLinkProcessor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "app", name = "db-access-type", havingValue = "jdbc")
public class JdbcPersistenceConfig {
    @Bean
    public LinkService getJdbcLinkService(JdbcScrapperRepository repository,
                                          JdbcStackAnswersRepository stackAnswersRepository,
                                          StackOverflowClient stackOverflowClient,
                                          GithubClient githubClient,
                                          @Qualifier("linkProcessorMap") Map<String, LinkProcessor> linkProcessors,
                                          Parser linkParser) {
        return new JdbcLinkService(repository, stackAnswersRepository, stackOverflowClient, githubClient, linkProcessors, linkParser);
    }

    @Bean
    public ChatService getJdbcChatService(JdbcScrapperRepository repository) {
        return new JdbcChatService(repository);
    }

    @Bean("scrapperJdbcTemplate")
    public NamedParameterJdbcTemplate getScrapperJdbcTemplate(@Qualifier("scrapperDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean
    public JdbcScrapperRepository getJdbcScrapperRepo(NamedParameterJdbcTemplate jdbcTemplate, ChatRowMapper chatRowMapper, LinksRowMapper linksRowMapper) {
        return new JdbcScrapperRepository(jdbcTemplate, chatRowMapper, linksRowMapper);
    }

    @Bean
    public JdbcStackAnswersRepository getSofAnswersRepo(NamedParameterJdbcTemplate jdbcTemplate) {
        return new JdbcStackAnswersRepository(jdbcTemplate);
    }

    @Bean
    public ChatRowMapper getChatRowMapper() {
        return new ChatRowMapper();
    }

    @Bean
    public LinksRowMapper getLinksRowMapper() {
        return new LinksRowMapper();
    }

    @Bean
    public JdbcGithubLinkProcessor getJdbcGhLinkProc(Parser linkParser, GithubClient githubClient, JdbcScrapperRepository scrapperRepository) {
        return new JdbcGithubLinkProcessor(linkParser, githubClient, scrapperRepository);
    }

    @Bean
    public JdbcStackOverflowLinkProcessor getJdbcSofLinkProc(Parser linkParser,
                                                             StackOverflowClient stackOverflowClient,
                                                             JdbcStackAnswersRepository answersRepository,
                                                             BotClient botClient,
                                                             JdbcScrapperRepository scrapperRepository) {
        return new JdbcStackOverflowLinkProcessor(linkParser, stackOverflowClient, answersRepository, botClient, scrapperRepository);
    }

    @Bean("linkProcessorMap")
    public Map<String, LinkProcessor> getJdbcLinkProcessors(JdbcGithubLinkProcessor githubLinkProcessor,
                                                            JdbcStackOverflowLinkProcessor stackOverflowLinkProcessor) {
        var res = new HashMap<String, LinkProcessor>();

        res.put(JdbcGithubLinkProcessor.HOST, githubLinkProcessor);
        res.put(JdbcStackOverflowLinkProcessor.HOST, stackOverflowLinkProcessor);

        log.info("Initializing jdbc link processors.");

        return res;
    }
}
