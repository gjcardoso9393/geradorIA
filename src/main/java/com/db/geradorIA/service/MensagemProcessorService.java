package com.db.geradorIA.service;

import com.db.geradorIA.dto.MensagemEvent;
import com.db.geradorIA.entity.Mensagem;
import com.db.geradorIA.entity.MensagemSla;
import com.db.geradorIA.repository.MensagemRepository;
import com.db.geradorIA.repository.MensagemSlaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class MensagemProcessorService {

    private static final String[] PALAVRAS_PROIBIDAS = {
            "spam",
            "fraude",
            "proibido"
    };

    @Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private MensagemSlaRepository mensagemSlaRepository;

    @Autowired
    private KafkaTemplate<String, MensagemEvent> kafkaTemplate;

    /**
     * Método principal de processamento da mensagem vinda do Kafka
     */
    public void processar(MensagemEvent event) {
        boolean temPalavraProibida = contemPalavraProibida(event.getMensagem());

        //sla
        MensagemSla novaMensagemSla = new MensagemSla();
        novaMensagemSla.setMensagemId(event.getMensagemId());
        if (temPalavraProibida) {

            mensagemRepository.atualizarEtapa(event.getMensagemId(), 3);

            novaMensagemSla.setEtapaId(3);
            novaMensagemSla.setObs("Mensagem com palavra proibida!");

            kafkaTemplate.send("image-request-dlq", event);
        } else {
            mensagemRepository.atualizarEtapa(event.getMensagemId(), 2);
            novaMensagemSla.setEtapaId(2);
            novaMensagemSla.setObs("Mensagem sem palavras proibidas!");

            kafkaTemplate.send("image-approved", event);
        }

        mensagemSlaRepository.save(novaMensagemSla);
    }

    /**
     * Valida se a mensagem contém palavras proibidas
     */
    private boolean contemPalavraProibida(String mensagem) {
        if (mensagem == null) return false;

        String texto = mensagem.toLowerCase();

        for (String palavra : PALAVRAS_PROIBIDAS) {
            if (texto.contains(palavra.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}