package com.db.geradorIA.consumer;

import com.db.geradorIA.dto.MensagemEvent;
import com.db.geradorIA.service.MensagemProcessorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ImageRequestConsumer {

    @Autowired
    private MensagemProcessorService mensagemProcessorService;

    @KafkaListener(topics = "image-request", groupId = "validador-service")
    public void consumir(MensagemEvent event) {
        mensagemProcessorService.processar(event);
    }
}