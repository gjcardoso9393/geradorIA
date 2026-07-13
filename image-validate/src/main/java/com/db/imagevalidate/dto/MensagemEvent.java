package com.db.imagevalidate.dto;

public class MensagemEvent {

    private Long mensagemId;
    private String mensagem;

    public MensagemEvent() {}

    public MensagemEvent(Long mensagemId, String mensagem) {
        this.mensagemId = mensagemId;
        this.mensagem = mensagem;
    }

    public Long getMensagemId() {
        return mensagemId;
    }

    public void setMensagemId(Long mensagemId) {
        this.mensagemId = mensagemId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}