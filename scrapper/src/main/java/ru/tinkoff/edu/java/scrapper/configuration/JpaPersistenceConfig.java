package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import ru.tinkoff.edu.java.parser.Parser;
import ru.tinkoff.edu.java.scrapper.client.GithubClient;
import ru.tinkoff.edu.java.scrapper.client.StackOverflowClient;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaChatRepository;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.ChatService;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.LinkService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcGithubLinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcStackOverflowLinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaGithubLinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaStackOverflowLinkProcessor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "db-access-type", havingValue = "jpa")
@Slf4j
public class JpaPersistenceConfig {

    @Bean
    public LinkService jpaLinkService(JpaLinkRepository linkRepository,
                               StackOverflowClient stackOverflowClient,
                               GithubClient githubClient,
                               Parser linkParser,
                               @Qualifier("linkProcessorMap") Map<String, LinkProcessor> linkProcessors) {
        return new JpaLinkService(linkRepository, stackOverflowClient, githubClient, linkParser, linkProcessors);
    }

    @Bean
    public ChatService jpaChatService(JpaChatRepository chatRepository) {
        return new JpaChatService(chatRepository);
    }

    @Bean("linkProcessorMap")
    public Map<String, LinkProcessor> getJpaLinkProcessors(JpaStackOverflowLinkProcessor stackOverflowLinkProcessor,
                                                           JpaGithubLinkProcessor githubLinkProcessor) {
        var res = new HashMap<String, LinkProcessor>();

        res.put(JdbcStackOverflowLinkProcessor.HOST, stackOverflowLinkProcessor);
        res.put(JdbcGithubLinkProcessor.HOST, githubLinkProcessor);

        return res;
    }

    @Bean
    public JpaStackOverflowLinkProcessor getJpaSofProc(StackOverflowClient stackOverflowClient, Parser linkParser, JpaLinkRepository linkRepository) {
        return new JpaStackOverflowLinkProcessor(stackOverflowClient, linkParser, linkRepository);
    }

    @Bean
    public JpaGithubLinkProcessor getJpaGhProc(Parser linkParser, GithubClient githubClient, JpaLinkRepository linkRepository) {
        return new JpaGithubLinkProcessor(linkParser, githubClient, linkRepository);
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Qualifier("scrapperDataSource") DataSource dataSource) {
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);

        LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

        factory.setJpaVendorAdapter(vendorAdapter);
        factory.setPackagesToScan("ru.tinkoff.edu.java.scrapper.repository.pojo");
        factory.setDataSource(dataSource);

        return factory;
    }
}
