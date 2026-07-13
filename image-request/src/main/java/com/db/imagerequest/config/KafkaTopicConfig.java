package com.db.imagerequest.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    private static final Logger log = LoggerFactory.getLogger(KafkaTopicConfig.class);

    @Bean
    public NewTopic imageRequestTopic() {

        return TopicBuilder.name("image-request")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic imageApprovedTopic() {

        return TopicBuilder.name("image-approved")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic imageDqlTopic() {

        return TopicBuilder.name("image-request-dlq")
                .partitions(1)
                .replicas(1)
                .build();
    }
}