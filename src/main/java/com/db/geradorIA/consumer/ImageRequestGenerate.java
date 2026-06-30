package com.db.geradorIA.consumer;

import com.db.geradorIA.dto.MensagemEvent;
import com.db.geradorIA.service.MensagemProcessorGenerate;
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