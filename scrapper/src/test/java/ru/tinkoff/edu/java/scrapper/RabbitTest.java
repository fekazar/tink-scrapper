package ru.tinkoff.edu.java.scrapper;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;

import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = "app.bot-client=rabbit")
@Testcontainers
public class RabbitTest extends IntegrationEnvironment {
    @Container
    static final GenericContainer RABBIT_CONTAINER = new GenericContainer("rabbitmq:management").withExposedPorts(5672);

    @DynamicPropertySource
    public static void setRabbitPort(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.port", () -> RABBIT_CONTAINER.getMappedPort(5672));
    }

    @Autowired
    private BotClient rabbitClient;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void checkNotNull() {
        assertNotNull(rabbitClient);
    }

    @Test
    @Timeout(value = 1000, unit = TimeUnit.SECONDS)
    void simpleSendTest() {
        rabbitClient.sendUpdates(new BotClient.LinkUpdate("hello msg", "test url", 1939233742));
        rabbitClient.sendUpdates(new BotClient.LinkUpdate("another hello message", "test url", 1939233742));
    }
}
