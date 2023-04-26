package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.jpa.JpaLinkRepository;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class JdbcLinkServiceTest extends JdbcIntegrationEnvironment {
    @Test
    void noForeignKeyTest() {
        assertThrows(Exception.class, () -> jdbcScrapperRepository.addLink("test_url", 51));
    }

    @Test
    void addLinkTest() {
        jdbcChatService.add(1);
        assertDoesNotThrow(() -> jdbcScrapperRepository.addLink("test_url", 1));
    }

    @Test
    void removeLinkTest() {
        jdbcChatService.add(1);
        assertDoesNotThrow(() -> jdbcScrapperRepository.addLink("test_url", 1));
        assertEquals(1, jdbcScrapperRepository.getAllLinks().size());
    }
}
