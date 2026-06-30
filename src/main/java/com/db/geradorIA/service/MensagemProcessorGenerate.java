package com.db.geradorIA.service;

import com.db.geradorIA.dto.MensagemEvent;
import com.db.geradorIA.entity.MensagemSla;
import com.db.geradorIA.repository.MensagemRepository;
import com.db.geradorIA.repository.MensagemSlaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class MensagemProcessorGenerate {

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
     * Método principal de geração da imagem
     */
    public void gerar(MensagemEvent event) {
        try {

            // URL da imagem aleatória
            String imageUrl ="https://picsum.photos/500?random=" + System.currentTimeMillis();
            String caminho = "assets/"+System.currentTimeMillis()+".jpg";

            // Abre conexão
            InputStream inputStream = new URL(imageUrl).openStream();
            Path destino = Path.of(caminho);

            // Salva arquivo
            Files.copy(
                    inputStream,
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );

            inputStream.close();

            System.out.println("Imagem salva com sucesso!");

            MensagemSla novaMensagemSla = new MensagemSla();
            novaMensagemSla.setMensagemId(event.getMensagemId());

            mensagemRepository.atualizarEtapa(event.getMensagemId(), 4);
            novaMensagemSla.setEtapaId(4);
            novaMensagemSla.setObs("Imagem Gerada");

            mensagemSlaRepository.save(novaMensagemSla);

            mensagemRepository.atualizarPath(event.getMensagemId(), caminho);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}