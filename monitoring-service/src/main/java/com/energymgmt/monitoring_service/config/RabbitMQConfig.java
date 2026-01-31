package com.energymgmt.monitoring_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "energy-sync-exchange";
    public static final String MONITORING_DEVICE_QUEUE = "monitoring-device-queue";
    public static final String NOTIFICATION_QUEUE = "notification_queue";

    @Value("${device.queue.name}")
    private String deviceQueueName;

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue deviceIngestQueue() {
        System.out.println("🔧 Creating Dynamic Queue: " + deviceQueueName);
        return new Queue(deviceQueueName, true);
    }

    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true);
    }

    @Bean
    public Queue deviceSyncQueue() {
        return new Queue(MONITORING_DEVICE_QUEUE, true);
    }

    @Bean
    public TopicExchange syncExchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    public Binding bindingDeviceSync() {
        return BindingBuilder.bind(deviceSyncQueue())
                .to(syncExchange())
                .with("device.#");
    }

    @Bean
    public TopicExchange sensorDataExchange() {
        return new TopicExchange("sensor-data-exchange");
    }

    @Bean
    public Binding bindingDeviceQueue(Queue deviceIngestQueue, TopicExchange sensorDataExchange) {
        return BindingBuilder
                .bind(deviceIngestQueue)
                .to(sensorDataExchange)
                .with("sensor.data." + deviceQueueName);
    }
}