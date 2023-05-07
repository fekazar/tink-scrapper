package ru.tinkoff.edu.java.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.tinkoff.edu.java.bot.server.request.LinkUpdateRequest;
import ru.tinkoff.edu.java.bot.tg.Bot;

@Component
@Slf4j
public class RabbitUpdatesHandler {
    @Autowired
    private Bot bot;

    @RabbitListener(queues = "${secrets.rabbit.queue}", messageConverter = "jsonMessageConverter")
    public void handleUpdate(LinkUpdateRequest request) {
        log.info("New request from rabbitmq.");
        bot.sendTextMessage(request.textMessage(), request.tgChatId());
    }
}
