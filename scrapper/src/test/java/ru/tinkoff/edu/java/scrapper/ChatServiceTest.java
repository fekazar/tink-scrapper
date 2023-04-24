package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
public class ChatServiceTest extends IntegrationEnvironment {
    @Test
    void addTest() {
        chatService.add(1);
        assertEquals(1, chatService.getAll().size());
        assertEquals(1, chatService.getAll().get(0).getId());
    }

    @Test
    void removeTest() {
        int id = 54;

        chatService.add(id);
        assertTrue(repository.hasChat(id));

        chatService.remove(id);
        assertFalse(repository.hasChat(id));
    }
}
