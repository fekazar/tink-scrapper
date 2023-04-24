package ru.tinkoff.edu.java.scrapper.configuration;

import jakarta.persistence.EntityManagerFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jca.support.LocalConnectionFactoryBean;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
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
