package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class LinksServiceTest extends IntegrationEnvironment {
    @Test
    void noForeignKeyTest() {
        assertThrows(Exception.class, () -> repository.addLink("test_url", 51));
    }

    @Test
    void addLinkTest() {
        chatService.add(1);
        assertDoesNotThrow(() -> repository.addLink("test_url", 1));
    }

    @Test
    void removeLinkTest() {
        chatService.add(1);
        assertDoesNotThrow(() -> repository.addLink("test_url", 1));
        assertEquals(1, repository.getAllLinks().size());
    }
}
