package com.db.geradorIA.repository;

import com.db.geradorIA.entity.Mensagem;
import org.hibernate.type.descriptor.jdbc.VarcharJdbcType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Text;

@Repository
public interface MensagemRepository extends JpaRepository<Mensagem, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE Mensagem m SET m.etapaId = :etapaId WHERE m.id = :id")
    void atualizarEtapa(@Param("id") Long id,
                        @Param("etapaId") Integer etapaId);

    @Modifying
    @Transactional
    @Query("UPDATE Mensagem m SET m.path = :path WHERE m.id = :id")
    void atualizarPath(@Param("id") Long id,
                       @Param("path") String etapa);

    boolean existsByMensagem(String mensagem);
}