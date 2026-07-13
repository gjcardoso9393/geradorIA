package com.db.imagevalidate.dto;

public record StatusResponse(
        Integer codigo,
        String status,
        String mensagem,
        Object dados
) {
}