package com.db.geradorIA.repository;

import com.db.geradorIA.entity.PalavraProibida;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PalavraProibidaRepository extends JpaRepository<PalavraProibida, Long> {
    boolean existsByPalavra(String palavra);
}