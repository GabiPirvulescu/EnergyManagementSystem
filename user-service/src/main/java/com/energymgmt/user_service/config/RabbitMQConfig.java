package com.energymgmt.user_service.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "energy-sync-exchange";
    public static final String USER_QUEUE = "user-service-sync-queue";

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(USER_QUEUE, true);
    }

    @Bean
    public Binding bindingUserSync() {
        return BindingBuilder.bind(userQueue()).to(syncExchange()).with("user.#");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}