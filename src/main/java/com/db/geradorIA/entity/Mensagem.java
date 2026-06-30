package com.db.geradorIA.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mensagem")
public class Mensagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mensagem_id")
    private Long mensagemId;

    @Column(name = "mensagem")
    private String mensagem;

    @Column(name = "path")
    private String path;

    @Column(name = "etapa_id")
    private Integer etapaId;

    public Long getMensagemId() {
        return mensagemId;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getEtapaId() {
        return etapaId;
    }

    public void setEtapaId(Integer etapaId) {
        this.etapaId = etapaId;
    }
}