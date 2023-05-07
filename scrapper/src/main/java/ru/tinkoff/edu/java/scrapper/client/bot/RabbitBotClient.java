package ru.tinkoff.edu.java.scrapper.client.bot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitBotClient implements BotClient {
    private final RabbitTemplate rabbit;

    public RabbitBotClient(RabbitTemplate rabbit) {
        this.rabbit = rabbit;
    }

    @Override
    public void sendUpdates(LinkUpdate body) {
        rabbit.convertAndSend(body);
    }
}
