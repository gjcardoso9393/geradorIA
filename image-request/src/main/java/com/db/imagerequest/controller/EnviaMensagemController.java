package com.db.imagerequest.controller;

import com.db.imagerequest.dto.EnviaMensagemRequest;
import com.db.imagerequest.dto.StatusResponse;
import com.db.imagerequest.entity.Mensagem;
import com.db.imagerequest.entity.MensagemSla;
import com.db.imagerequest.repository.MensagemRepository;
import com.db.imagerequest.repository.MensagemSlaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.db.imagerequest.dto.MensagemEvent;
import org.springframework.kafka.core.KafkaTemplate;

@RestController
@RequestMapping("/api")
@EnableKafka
@SpringBootApplication
public class EnviaMensagemController {
    //implementar depois
    private static final String TOKEN_VALIDO = "123456";

    private final MensagemRepository mensagemRepository;
    private MensagemRepository repository;


    private final MensagemSlaRepository mensagemSlaRepository;
    private MensagemSlaRepository repositorysla;


    private final KafkaTemplate<String, MensagemEvent> kafkaTemplate;

    public EnviaMensagemController(
            MensagemRepository mensagemRepository,
            MensagemSlaRepository mensagemSlaRepository,
            KafkaTemplate<String, MensagemEvent> kafkaTemplate) {

        this.mensagemRepository = mensagemRepository;
        this.mensagemSlaRepository = mensagemSlaRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @PostMapping("/envia-mensagem")
    public ResponseEntity<StatusResponse> enviarMensagem(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody EnviaMensagemRequest request) {

        // Validação do token
        if (token == null || !token.equals(TOKEN_VALIDO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Token inválido",
                            null
                    ));
        }

        // Validação da mensagem
        if (request.mensagem() == null || request.mensagem().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StatusResponse(
                            400,
                            "ERRO",
                            "Mensagem não informada",
                            null
                    ));
        }
        //verificando se a mensagem ja existe
        if (mensagemRepository.existsByMensagem(request.mensagem())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StatusResponse(
                            409,
                            "ERRO",
                            "Mensagem já cadastrada",
                            null
                    ));
        }

        // Salva no banco
        Mensagem novaMensagem = new Mensagem();
        novaMensagem.setMensagem(request.mensagem());
        novaMensagem.setEtapaId(1);
        //mensagemRepository.saveAndFlush(novaMensagem);
        //System.out.println("ID SALVO: " + novaMensagem.getMensagemId());
        mensagemRepository.save(novaMensagem);

        //sla
        MensagemSla novaMensagemSla = new MensagemSla();
        novaMensagemSla.setMensagemId(novaMensagem.getMensagemId());
        novaMensagemSla.setEtapaId(1);
        novaMensagemSla.setObs("Mensagem criada na fila!");
        mensagemSlaRepository.save(novaMensagemSla);


        //Envia pro kafka

        MensagemEvent event = new MensagemEvent();
        event.setMensagemId(novaMensagem.getMensagemId());
        event.setMensagem(novaMensagem.getMensagem());
        kafkaTemplate.send("image-request", event);


        // Retorna sucesso
        return ResponseEntity.ok(
                new StatusResponse(
                        200,
                        "SUCESSO",
                        "Mensagem salva com sucesso",
                        request.mensagem()
                )
        );
    }
}