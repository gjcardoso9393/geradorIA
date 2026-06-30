package com.db.geradorIA.controller;

import com.db.geradorIA.dto.SalvaPalavraRequest;
import com.db.geradorIA.dto.StatusResponse;

import com.db.geradorIA.entity.PalavraProibida;
import com.db.geradorIA.repository.PalavraProibidaRepository;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@EnableKafka
@SpringBootApplication
public class SalvaPalavraProibidaController {
    //implementar depois
    private static final String TOKEN_VALIDO = "123456";
    private static final String TOKEN_VALIDO_SEM_PERMISSAO = "1234567";



    private final PalavraProibidaRepository palavraProibidaRepository;
    private PalavraProibidaRepository repositorypalavraproibida;



    public SalvaPalavraProibidaController(
            PalavraProibidaRepository palavraProibidaRepository) {

        this.palavraProibidaRepository = palavraProibidaRepository;
    }


    @GetMapping("/palavras")
    public ResponseEntity<?> listarPalavras(
            @RequestHeader(value = "Authorization", required = false) String token) {

        if (token == null || (!token.equals(TOKEN_VALIDO) && !token.equals(TOKEN_VALIDO_SEM_PERMISSAO))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Token inválido",
                            null
                    ));
        }

        return ResponseEntity.ok(
                new StatusResponse(
                        200,
                        "SUCESSO",
                        "Lista de Palvras",
                        palavraProibidaRepository.findAll()
                )
        );


    }

    @DeleteMapping("/palavras_delete/{id}")
    public ResponseEntity<?> deletarPalavra(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {

        // Validação do token

        if (token == null || (!token.equals(TOKEN_VALIDO) && !token.equals(TOKEN_VALIDO_SEM_PERMISSAO))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Token inválido",
                            null
                    ));
        }else if(token.equals(TOKEN_VALIDO_SEM_PERMISSAO)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StatusResponse(
                    401,
                    "ERRO",
                    "Rota sem Autorização",
                    null
            ));
        }

        if (!palavraProibidaRepository.existsById(id)) {

            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new StatusResponse(
                            404,
                            "ERRO",
                            "Palavra não encontrada",
                            null
                    ));
        }

        palavraProibidaRepository.deleteById(id);

        return ResponseEntity.ok(
                new StatusResponse(
                        200,
                        "SUCESSO",
                        "Palavra removida com sucesso",
                        null
                )
        );
    }

    @PostMapping("/salva-palavra")
    public ResponseEntity<StatusResponse> salvarPalavra(
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody SalvaPalavraRequest request) {

        // Validação do token

        if (token == null || (!token.equals(TOKEN_VALIDO) && !token.equals(TOKEN_VALIDO_SEM_PERMISSAO))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Token inválido",
                            null
                    ));
        }else if(token.equals(TOKEN_VALIDO_SEM_PERMISSAO)){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new StatusResponse(
                    401,
                    "ERRO",
                    "Rota sem Autorização",
                    null
            ));
        }

        // Validação da mensagem
        if (request.palavra() == null || request.palavra().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StatusResponse(
                            400,
                            "ERRO",
                            "Palavra não informada",
                            null
                    ));
        }
        //verificando se a mensagem ja existe
        if (palavraProibidaRepository.existsByPalavra(request.palavra().toLowerCase())) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StatusResponse(
                            409,
                            "ERRO",
                            "Palavra já cadastrada",
                            null
                    ));
        }

        //sla
        PalavraProibida novaPalavraProibida = new PalavraProibida();
        novaPalavraProibida.setPalavra(request.palavra().toLowerCase());
        palavraProibidaRepository.save(novaPalavraProibida);

        // Retorna sucesso
        return ResponseEntity.ok(
                new StatusResponse(
                        200,
                        "SUCESSO",
                        "Palavra salva com sucesso",
                        request.palavra()
                )
        );
    }
}