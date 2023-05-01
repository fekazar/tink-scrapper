package ru.tinkoff.edu.java.scrapper.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
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
                                         @Value("${secrets.rabbit.routing_key}") String routingKey) {
        var rabbit = new RabbitTemplate(connectionFactory);
        rabbit.setRoutingKey(routingKey);
        rabbit.setMessageConverter(jsonMessageConverter());

        return rabbit;
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
