package ru.tinkoff.edu.java.bot.configuration;

import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
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
import ru.tinkoff.edu.java.bot.server.request.LinkUpdateRequest;

import java.util.HashMap;

@Slf4j
@Configuration
public class RabbitConfig {
    public static final String DLQ = ".dlq";

    @Bean
    public Queue scrapperQueue(@Value("${secrets.rabbit.queue}") String queueName,
                               @Value("${secrets.rabbit.exchange}") String exchangeName,
                               @Value("${secrets.rabbit.routing_key}") String routingKey) {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", exchangeName + DLQ)
                .withArgument("x-dead-letter-routing-key", routingKey)
                .build();
    }

    @Bean
    public Queue scrapperDLQueue(@Value("${secrets.rabbit.queue}") String queueName) {
        return QueueBuilder
                .nonDurable(queueName + DLQ)
                .build();
    }

    @Bean
    public DirectExchange scrapperExchange(@Value("${secrets.rabbit.exchange}") String exchangeName) {
        return ExchangeBuilder.directExchange(exchangeName).build();
    }

    @Bean
    public FanoutExchange scrapperDLExchange(@Value("${secrets.rabbit.exchange}") String exchangeName) {
        return ExchangeBuilder.fanoutExchange(exchangeName + DLQ).build();
    }

    @Bean
    public Binding scrapperBinding(@Qualifier("scrapperQueue") Queue queue,
                                   @Qualifier("scrapperExchange") DirectExchange exchange,
                                   @Value("${secrets.rabbit.routing_key}") String routingKey) {
        log.info("Create binding: " + queue.getName() + "-" + exchange.getName());
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Binding scrapperDLBinding(@Qualifier("scrapperDLQueue") Queue queue,
                                   @Qualifier("scrapperDLExchange") FanoutExchange exchange,
                                   @Value("${secrets.rabbit.routing_key}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange);
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
    public SimpleRabbitListenerContainerFactory containerFactory(ConnectionFactory connectionFactory) {
        var factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jsonMessageConverter());
        return factory;
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
        map.put("LinkUpdate", LinkUpdateRequest.class);

        var mapper = new DefaultClassMapper();
        mapper.setIdClassMapping(map);

        return mapper;
    }
}
