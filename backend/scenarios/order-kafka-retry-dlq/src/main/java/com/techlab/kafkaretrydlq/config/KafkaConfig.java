package com.techlab.kafkaretrydlq.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

@Configuration
@EnableConfigurationProperties(AppKafkaProperties.class)
public class KafkaConfig {

    @Bean
    public NewTopic orderCreatedTopic(AppKafkaProperties properties) {
        return TopicBuilder
                .name(properties.getTopics().getOrderCreated())
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public KafkaAdmin.NewTopics retryAndDltTopics(AppKafkaProperties properties) {
        String mainTopic = properties.getTopics().getOrderCreated();
        NewTopic firstRetry = TopicBuilder.name(mainTopic + ".retry-0").partitions(3).replicas(1).build();
        NewTopic secondRetry = TopicBuilder.name(mainTopic + ".retry-1").partitions(3).replicas(1).build();
        NewTopic dltTopic = TopicBuilder.name(mainTopic + ".dlt").partitions(3).replicas(1).build();
        return new KafkaAdmin.NewTopics(firstRetry, secondRetry, dltTopic);
    }
}
