package com.db.geradorIA.producer;

import com.db.geradorIA.dto.MensagemEvent;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class MensagemProducer {

    private final KafkaTemplate<String, MensagemEvent> kafkaTemplate;

    public MensagemProducer(KafkaTemplate<String, MensagemEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void enviarMensagem(String topic, MensagemEvent event) {
        kafkaTemplate.send(topic, event);
    }
}