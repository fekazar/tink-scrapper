package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import ru.tinkoff.edu.java.scrapper.service.LinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcGithubLinkProcessor;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcStackOverflowLinkProcessor;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@PropertySource("classpath:secrets.properties")
public class PersistenceConfig {
    @Bean("scrapperDataSource")
    public DataSource getScrapperDataSource(
            @Value("${secrets.db_url}") String url,
            @Value("${secrets.db_user}") String user,
            @Value("${secrets.db_pass}") String password
    ) {
        log.info("Url for scrapper datasource: " + url);
        return DataSourceBuilder.create()
                .url(url)
                .username(user)
                .password(password)
                .build();
    }

    @Bean("scrapperJdbcTemplate")
    public NamedParameterJdbcTemplate getScrapperJdbcTemplate(@Qualifier("scrapperDataSource") DataSource dataSource) {
        return new NamedParameterJdbcTemplate(dataSource);
    }

    @Bean("jdbcLinkProcessors")
    public Map<String, LinkProcessor> getJdbcLinkProcessors(JdbcGithubLinkProcessor githubLinkProcessor,
                                                            JdbcStackOverflowLinkProcessor stackOverflowLinkProcessor) {
        var res = new HashMap<String, LinkProcessor>();

        res.put(JdbcGithubLinkProcessor.HOST, githubLinkProcessor);
        res.put(JdbcStackOverflowLinkProcessor.HOST, stackOverflowLinkProcessor);

        return res;
    }
}
