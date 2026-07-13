package com.db.imagevalidate.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "palavra_proibida")
public class PalavraProibida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "palavra_proibida_id")
    private Long palavraProibidaId;


    @Column(name = "palavra")
    private String palavra;

    public Long getPalavraProibidaId() {
        return palavraProibidaId;
    }

    public void setPalavraProibidaId(Long palavraProibidaId) {
        this.palavraProibidaId = palavraProibidaId;
    }

    public String getPalavra() {
        return palavra;
    }

    public void setPalavra(String palavra) {
        this.palavra = palavra;
    }
}