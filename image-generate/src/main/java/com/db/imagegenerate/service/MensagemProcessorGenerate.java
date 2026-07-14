package com.db.imagegenerate.service;

import com.db.imagegenerate.dto.MensagemEvent;
import com.db.imagegenerate.entity.MensagemSla;
import com.db.imagegenerate.entity.Mensagem;
import com.db.imagegenerate.repository.MensagemRepository;
import com.db.imagegenerate.repository.MensagemSlaRepository;
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
            Path pastaAssets = Path.of("assets");

            Files.createDirectories(pastaAssets);


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

            Mensagem novaMensagem = new Mensagem();
            novaMensagem.setMensagem(event.getMensagem());
            novaMensagem.setEtapaId(4);
            novaMensagem.setPath(caminho);
            //mensagemRepository.saveAndFlush(novaMensagem);
            //System.out.println("ID SALVO: " + novaMensagem.getMensagemId());
            mensagemRepository.save(novaMensagem);


            MensagemSla novaMensagemSla = new MensagemSla();
            novaMensagemSla.setMensagemId(novaMensagem.getMensagemId());

            novaMensagemSla.setEtapaId(4);
            novaMensagemSla.setObs("Imagem Gerada");

            mensagemSlaRepository.save(novaMensagemSla);




            //mensagemRepository.atualizarEtapa(event.getMensagemId(), 4);
           // mensagemRepository.atualizarPath(event.getMensagemId(), caminho);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}