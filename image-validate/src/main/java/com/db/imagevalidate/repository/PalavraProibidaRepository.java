package com.db.imagevalidate.repository;

import com.db.imagevalidate.entity.PalavraProibida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalavraProibidaRepository extends JpaRepository<PalavraProibida, Long> {
    boolean existsByPalavra(String palavra);
}