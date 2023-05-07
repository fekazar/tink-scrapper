package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@TestPropertySource(properties = "app.db-access-type=jdbc")
public class JdbcChatServiceTest extends JdbcIntegrationEnvironment {
    @Test
    void addTest() {
        jdbcChatService.add(1);
        assertEquals(1, jdbcChatService.getAll().size());
        assertEquals(1, jdbcChatService.getAll().get(0).getId());

        jdbcChatService.remove(1);
        assertEquals(0, jdbcChatService.getAll().size());
    }

    @Test
    void removeTest() {
        int id = 54;

        jdbcChatService.add(id);
        assertTrue(jdbcScrapperRepository.hasChat(id));

        jdbcChatService.remove(id);
        assertFalse(jdbcScrapperRepository.hasChat(id));
    }
}
