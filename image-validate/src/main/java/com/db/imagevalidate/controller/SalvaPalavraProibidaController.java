package com.db.imagevalidate.controller;

import com.db.imagevalidate.dto.SalvaPalavraRequest;
import com.db.imagevalidate.dto.StatusResponse;
import com.db.imagevalidate.entity.PalavraProibida;
import com.db.imagevalidate.repository.PalavraProibidaRepository;
import com.db.imagevalidate.service.PalavraProibidaCacheService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class SalvaPalavraProibidaController {

    private static final String TOKEN_VALIDO = "123456";
    private static final String TOKEN_VALIDO_SEM_PERMISSAO = "1234567";

    private final PalavraProibidaRepository palavraProibidaRepository;
    private final PalavraProibidaCacheService cacheService;

    public SalvaPalavraProibidaController(
            PalavraProibidaRepository palavraProibidaRepository,
            PalavraProibidaCacheService cacheService) {

        this.palavraProibidaRepository = palavraProibidaRepository;
        this.cacheService = cacheService;
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
                        "Lista de Palavras",
                        cacheService.buscarPalavras()
                        //palavraProibidaRepository.findAll()
                )
        );
    }

    @DeleteMapping("/palavras_delete/{id}")
    public ResponseEntity<?> deletarPalavra(
            @RequestHeader(value = "Authorization", required = false) String token,
            @PathVariable Long id) {

        if (token == null || (!token.equals(TOKEN_VALIDO) && !token.equals(TOKEN_VALIDO_SEM_PERMISSAO))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Token inválido",
                            null
                    ));
        }

        if (token.equals(TOKEN_VALIDO_SEM_PERMISSAO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Rota sem autorização",
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

        // Atualiza o cache
        cacheService.atualizarCache();

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

        if (token == null || (!token.equals(TOKEN_VALIDO) && !token.equals(TOKEN_VALIDO_SEM_PERMISSAO))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Token inválido",
                            null
                    ));
        }

        if (token.equals(TOKEN_VALIDO_SEM_PERMISSAO)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new StatusResponse(
                            401,
                            "ERRO",
                            "Rota sem autorização",
                            null
                    ));
        }

        if (request.palavra() == null || request.palavra().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new StatusResponse(
                            400,
                            "ERRO",
                            "Palavra não informada",
                            null
                    ));
        }

        if (palavraProibidaRepository.existsByPalavra(request.palavra().toLowerCase())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(new StatusResponse(
                            409,
                            "ERRO",
                            "Palavra já cadastrada",
                            null
                    ));
        }

        PalavraProibida novaPalavraProibida = new PalavraProibida();
        novaPalavraProibida.setPalavra(request.palavra().toLowerCase());

        palavraProibidaRepository.save(novaPalavraProibida);

        // Atualiza o cache
        cacheService.atualizarCache();

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