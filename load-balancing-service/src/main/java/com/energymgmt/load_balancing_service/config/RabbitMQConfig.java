package com.energymgmt.load_balancing_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue sensorDataQueue() {
        System.out.println("🔧 Creating sensor-data-queue");
        return new Queue("sensor-data-queue", true);
    }

    @Bean
    public Queue ingestQueue0() {
        System.out.println("🔧 Creating ingest_queue_0");
        return new Queue("ingest_queue_0", true);
    }

    @Bean
    public Queue ingestQueue1() {
        System.out.println("🔧 Creating ingest_queue_1");
        return new Queue("ingest_queue_1", true);
    }

    @Bean
    public Queue ingestQueue2() {
        System.out.println("🔧 Creating ingest_queue_2");
        return new Queue("ingest_queue_2", true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new JacksonJsonMessageConverter();
    }
}