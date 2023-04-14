package ru.tinkoff.edu.java.scrapper;

import jakarta.annotation.PostConstruct;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.DirectoryResourceAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;
import java.nio.file.Path;

@ContextConfiguration(classes = IntegrationEnvironment.Config.class)
public abstract class IntegrationEnvironment {
    public static final PostgreSQLContainer POSTGRES_CONTAINER;

    @Autowired
    protected DataSource postgresDataSource;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer("postgres:latest");
        POSTGRES_CONTAINER.withDatabaseName("scrapper");
        POSTGRES_CONTAINER.start();
    }

    @Configuration
    public static class Config {
        @Bean("postgresDataSource")
        public DataSource postgresDataSource() {
            return DataSourceBuilder
                    .create()
                    .url(POSTGRES_CONTAINER.getJdbcUrl())
                    .username(POSTGRES_CONTAINER.getUsername())
                    .password(POSTGRES_CONTAINER.getPassword())
                    .build();
        }

        @Bean
        public Liquibase getLiquibase(DataSource postgresDataSource) throws Exception {
            var connection = postgresDataSource().getConnection();

            liquibase.database.Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new liquibase.Liquibase("master.xml", new DirectoryResourceAccessor(Path.of("migrations")), database);
            System.out.println(liquibase.getDatabaseChangeLog().getFilePath());
            liquibase.update(new Contexts(), new LabelExpression());

            return liquibase;
        }
    }
}
