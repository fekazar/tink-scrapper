package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.jdbc.JdbcScrapperRepository;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcChatService;
import ru.tinkoff.edu.java.scrapper.service.jdbc.JdbcLinkService;

@Slf4j
@SpringBootTest
@Transactional
@TestPropertySource(properties = "app.db-access-type=jdbc")
public class JdbcIntegrationEnvironment extends IntegrationEnvironment {
    @Autowired
    JdbcScrapperRepository jdbcScrapperRepository;

    @Autowired
    JdbcChatService jdbcChatService;

    @Autowired
    JdbcLinkService jdbcLinkService;
}
