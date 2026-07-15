package com.db.imagevalidate.service;

import com.db.imagevalidate.dto.MensagemEvent;
import com.db.imagevalidate.repository.PalavraProibidaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class MensagemProcessorValidador {
    @Autowired
    private PalavraProibidaCacheService cacheService;


    /*@Autowired
    private MensagemRepository mensagemRepository;

    @Autowired
    private MensagemSlaRepository mensagemSlaRepository;*/

    @Autowired
    private KafkaTemplate<String, MensagemEvent> kafkaTemplate;

    /**
     * Método principal de processamento da mensagem vinda do Kafka
     */
    public void processar(MensagemEvent event) {
        boolean temPalavraProibida = contemPalavraProibida(event.getMensagem());

        //sla
       /* MensagemSla novaMensagemSla = new MensagemSla();
        novaMensagemSla.setMensagemId(event.getMensagemId());*/
        if (temPalavraProibida) {

            /*mensagemRepository.atualizarEtapa(event.getMensagemId(), 3);

            novaMensagemSla.setEtapaId(3);
            novaMensagemSla.setObs("Mensagem com palavra proibida!");*/

            kafkaTemplate.send("image-request-dlq", event);
        } else {
           /* mensagemRepository.atualizarEtapa(event.getMensagemId(), 2);
            novaMensagemSla.setEtapaId(2);
            novaMensagemSla.setObs("Mensagem sem palavras proibidas!");*/

            kafkaTemplate.send("image-approved", event);
        }

        //mensagemSlaRepository.save(novaMensagemSla);
    }

    /**
     * Valida se a mensagem contém palavras proibidas
     */
        private boolean contemPalavraProibida(String mensagem){

            List<String> palavras = cacheService.buscarPalavras();

            String texto = mensagem.toLowerCase();

            return palavras.stream()
                    .map(String::toLowerCase)
                    .anyMatch(texto::contains);

        }
}