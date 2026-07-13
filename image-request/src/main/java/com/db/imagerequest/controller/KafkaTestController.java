package com.db.imagerequest.controller;

import com.db.imagerequest.dto.MensagemEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KafkaTestController {

    @Autowired
    private KafkaTemplate<String, MensagemEvent> kafkaTemplate;

    @GetMapping("/kafka-test")
    public String testarKafka() {

        MensagemEvent event = new MensagemEvent(1L, "teste kafka");

        kafkaTemplate.send("image-request", event);

        return "Mensagem enviada para Kafka!";
    }
}