package com.senac.aulaFull.domin.repository;

import com.senac.aulaFull.domin.entites.Transacao;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.enums.TipoTransacao;
import com.senac.aulaFull.domin.interfaces.SomaPorTipoResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransacaoRepository extends JpaRepository <Transacao,Long> {


    Optional<Transacao> findByIdAndUsuario(Long id, Usuario usuario);

    List<Transacao> findByUsuarioAndDataBetween(Usuario usuario, LocalDate dataInicial, LocalDate dataFinal);

    List<Transacao> findByUsuario(Usuario usuario);

    @Query("""
        SELECT t.tipo AS tipo, SUM(t.valor) AS total
            FROM Transacao t
            WHERE t.usuario = :usuario
                AND t.tipo= :tipo
                AND MONTH(t.data) = :mes
                AND YEAR(t.data) = :ano
                GROUP BY t.tipo""")
    List<SomaPorTipoResponse> buscarSomaPorTipoMes(TipoTransacao tipo, @Param("mes") int mes, @Param("ano") int ano, @Param("usuario") Usuario  usuario);

    @Query("""
     SELECT COALESCE(SUM(t.valor), 0)
        FROM Transacao t
        WHERE t.usuario = :usuario
            AND t.tipo = :tipo
            AND MONTH(t.data) = :mes
            AND YEAR(t.data) = :ano """)
    Double buscarTotalPorTipo( @Param("usuario") Usuario  usuario,@Param("tipo") TipoTransacao tipo, @Param("mes") int mes, @Param("ano") int ano);


    @Query("""
            SELECT t
                FROM Transacao t
                WHERE t.usuario = :usuario
                    AND MONTH(t.data) = :mes
                    AND YEAR(t.data)=:ano
                    AND (:tipo IS NULL OR t.tipo = :tipo)
            """)
    List<Transacao> findByUsuarioAndMesAno(
            @Param("usuario") Usuario  usuario,
            @Param("mes") Integer mes,
            @Param("ano") Integer ano,
            @Param("tipo") TipoTransacao tipo
    );


}
