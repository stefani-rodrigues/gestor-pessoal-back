package com.senac.aulaFull.repository;

import com.senac.aulaFull.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository <Transacao,Long> {

    @Override
    Optional<Transacao> findById(Long id);

    @Query("SELECT COALESCE(SUM(t.valor), 0.0) FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo = 'RECEITA'")
    Double sumReceitasByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT COALESCE(SUM(t.valor),0.0)FROM Transacao t WHERE t.usuario.id= :usuarioId AND t.tipo ='DESPESA'")
    Double sumDespesasByUsuarioId(@Param("usuarioId") Long usuarioId);

    @Query("SELECT (SUM(t.valor),0.0)FROM Transacao t WHERE t.usuario.id = :usuarioId AND t.tipo ='INVESTIMENTO'")
    Double sumInvestimentoByUsuarioId(@Param("usuarioId") Long usuarioId);
}
