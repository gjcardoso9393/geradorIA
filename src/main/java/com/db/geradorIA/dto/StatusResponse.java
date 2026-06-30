package com.db.geradorIA.dto;

public record StatusResponse(
        Integer codigo,
        String status,
        String mensagem,
        Object dados
) {
}