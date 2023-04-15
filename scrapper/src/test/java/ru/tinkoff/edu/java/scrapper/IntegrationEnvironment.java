package ru.tinkoff.edu.java.scrapper;

import jakarta.annotation.PostConstruct;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;

import javax.sql.DataSource;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;

@Slf4j
@TestComponent
public abstract class IntegrationEnvironment {
    public static final PostgreSQLContainer POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:latest");
        POSTGRES_CONTAINER.withDatabaseName("scrapper");
        POSTGRES_CONTAINER.start();

        var postgresDataSource = DataSourceBuilder
                .create()
                .url(POSTGRES_CONTAINER.getJdbcUrl())
                .username(POSTGRES_CONTAINER.getUsername())
                .password(POSTGRES_CONTAINER.getPassword())
                .build();

        try {
            var connection = postgresDataSource.getConnection();

            liquibase.database.Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase("master.xml", new DirectoryResourceAccessor(Path.of("migrations")), database);
            liquibase.update(new Contexts(), new LabelExpression());
            liquibase.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    JdbcScrapperRepository repository;

    @Autowired
    JdbcChatService chatService;

    @Autowired
    JdbcLinkService linkService;

    @DynamicPropertySource
    public static void setDbProperties(DynamicPropertyRegistry registry) {
        registry.add("secrets.db_url", POSTGRES_CONTAINER::getJdbcUrl);
        registry.add("secrets.db_user", POSTGRES_CONTAINER::getUsername);
        registry.add("secrets.db_pass", POSTGRES_CONTAINER::getPassword);
    }
}
