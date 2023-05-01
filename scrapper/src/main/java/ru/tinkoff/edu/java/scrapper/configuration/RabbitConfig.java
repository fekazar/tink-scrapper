package ru.tinkoff.edu.java.scrapper.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.tinkoff.edu.java.scrapper.client.bot.BotClient;

import java.util.HashMap;

@Configuration
@Slf4j
public class RabbitConfig {
    @Bean
    public Queue scrapperQueue(@Value("${secrets.rabbit.queue}") String queueName) {
        return QueueBuilder.durable(queueName).build();
    }

    @Bean
    public DirectExchange scrapperExchange(@Value("${secrets.rabbit.exchange}") String exchangeName) {
        return ExchangeBuilder.directExchange(exchangeName).build();
    }

    @Bean
    public Binding scrapperBinding(@Qualifier("scrapperQueue") Queue queue,
                                   @Qualifier("scrapperExchange") DirectExchange exchange,
                                   @Value("${secrets.rabbit.routing_key}") String routingKey) {
        log.info("Create binding: " + queue.getName() + "-" + exchange.getName());
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public ConnectionFactory cachingConnectionFactory(@Value("${secrets.rabbit.host}") String hostName) {
        var factory = new CachingConnectionFactory(hostName);
        return factory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(@Qualifier("cachingConnectionFactory") ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(@Qualifier("cachingConnectionFactory") ConnectionFactory connectionFactory,
                                         @Value("${secrets.rabbit.routing_key}") String routingKey,
                                         @Value("${secrets.rabbit.exchange}") String exchange) {
        var rabbit = new RabbitTemplate(connectionFactory);

        rabbit.setRoutingKey(routingKey);
        rabbit.setMessageConverter(jsonMessageConverter());
        rabbit.setExchange(exchange);

        return rabbit;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        var converter = new Jackson2JsonMessageConverter();
        converter.setClassMapper(classMapper());
        return converter;
    }

    @Bean
    public ClassMapper classMapper() {
        var map = new HashMap<String, Class<?>>();
        map.put("LinkUpdate", BotClient.LinkUpdate.class);

        var mapper = new DefaultClassMapper();
        mapper.setIdClassMapping(map);

        return mapper;
    }
}
