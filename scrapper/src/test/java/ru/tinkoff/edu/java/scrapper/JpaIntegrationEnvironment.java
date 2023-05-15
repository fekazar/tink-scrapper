package ru.tinkoff.edu.java.scrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaChatService;
import ru.tinkoff.edu.java.scrapper.service.jpa.JpaLinkService;

@SpringBootTest
@Transactional
@TestPropertySource(properties = "app.db-access-type=jpa")
public class JpaIntegrationEnvironment extends IntegrationEnvironment {
    @Autowired
    JpaLinkRepository jpaLinkRepository;

    @Autowired
    JpaLinkService jpaLinkService;

    @Autowired
    JpaChatService jpaChatService;
}
