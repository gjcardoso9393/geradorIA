package com.db.imagerequest.dto;

public record StatusResponse(
        Integer codigo,
        String status,
        String mensagem,
        Object dados
) {
}