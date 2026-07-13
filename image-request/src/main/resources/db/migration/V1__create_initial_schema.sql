CREATE TABLE etapa (
                       id BIGSERIAL PRIMARY KEY,
                       nome VARCHAR(255) NOT NULL
);

CREATE TABLE mensagem (
                          id BIGSERIAL PRIMARY KEY,
                          conteudo TEXT,
                          etapa_id BIGINT REFERENCES etapa(id)
);