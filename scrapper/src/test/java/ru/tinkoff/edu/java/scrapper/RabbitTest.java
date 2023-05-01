package ru.tinkoff.edu.java.scrapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "app.bot-client=rabbit")
public class RabbitTest {
    @Autowired
    @Qualifier("rabbitmqBotClient")
    private BotClient rabbitClient;

    @Test
    void checkNotNull() {
        assertNotNull(rabbitClient);
    }
}
