package com.db.imagevalidate.consumer;

import com.db.imagevalidate.dto.MensagemEvent;
import com.db.imagevalidate.service.MensagemProcessorValidador;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ImageRequestConsumer {

    @Autowired
    private MensagemProcessorValidador mensagemProcessorService;

    @KafkaListener(topics = "image-request", groupId = "validador-service")
    public void consumir(MensagemEvent event) {
        mensagemProcessorService.processar(event);
    }
}