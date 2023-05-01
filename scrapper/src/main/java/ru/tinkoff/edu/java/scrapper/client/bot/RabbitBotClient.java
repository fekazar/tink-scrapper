package ru.tinkoff.edu.java.scrapper.client.bot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

public class RabbitBotClient implements BotClient {
    private final RabbitTemplate rabbit;
    private final String routingKey;

    public RabbitBotClient(RabbitTemplate rabbit, String routingKey) {
        this.rabbit = rabbit;
        this.routingKey = routingKey;
    }

    @Override
    public void sendUpdates(LinkUpdate body) {
        rabbit.convertAndSend(body);
    }
}
