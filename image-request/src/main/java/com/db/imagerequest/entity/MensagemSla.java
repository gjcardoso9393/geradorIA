package com.db.imagerequest.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "mensagem_sla")
public class MensagemSla {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mensagem_sla_id")
    private Long mensagemSlaId;

    @Column(name = "mensagem_id")
    private Long mensagemId;

    @Column(name = "etapa_id")
    private Integer etapaId;

    @Column(name = "obs")
    private String obs;

    public Long getMensagemSlaId() {
        return mensagemSlaId;
    }

    public void setMensagemSlaId(Long mensagemSlaId) {
        this.mensagemSlaId = mensagemSlaId;
    }

    public Long getMensagemId() {
        return mensagemId;
    }

    public void setMensagemId(Long mensagemId) {
        this.mensagemId = mensagemId;
    }

    public Integer getEtapaId() {
        return etapaId;
    }

    public void setEtapaId(Integer etapaId) {
        this.etapaId = etapaId;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }
}