package ru.tinkoff.edu.java.scrapper;

import java.time.OffsetDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.tinkoff.edu.java.scrapper.repository.pojo.Link;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

        var link = new Link(1, "test_url_1", 1);
        link.setLastUpdate(OffsetDateTime.now());

        jdbcScrapperRepository.addTypedLink(link, "default");

        var links = jdbcScrapperRepository.getAllLinks();
        assertEquals(1, links.size());
    }

    @Test
    void removeLinkTest() {
        jdbcChatService.add(1);
        assertDoesNotThrow(() -> jdbcScrapperRepository.addLink("test_url", 1));
        assertEquals(1, jdbcScrapperRepository.getAllLinks().size());
    }
}
