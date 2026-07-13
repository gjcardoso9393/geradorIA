package com.db.imagevalidate.consumer;

import com.db.imagevalidate.dto.MensagemEvent;
import com.db.imagevalidate.service.MensagemProcessorGenerate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ImageRequestGenerate {

    @Autowired
    private MensagemProcessorGenerate MensagemProcessorGenerate;

    @KafkaListener(topics = "image-approved", groupId = "validador-service")
    public void consumir(MensagemEvent event) {
        MensagemProcessorGenerate.gerar(event);
    }
}