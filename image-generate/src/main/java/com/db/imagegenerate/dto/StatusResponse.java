package com.db.imagegenerate.dto;

public record StatusResponse(
        Integer codigo,
        String status,
        String mensagem,
        Object dados
) {
}