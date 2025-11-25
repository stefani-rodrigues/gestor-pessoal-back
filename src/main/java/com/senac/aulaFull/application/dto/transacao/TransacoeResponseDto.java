package com.senac.aulaFull.application.dto.transacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senac.aulaFull.domin.entites.Transacao;
import com.senac.aulaFull.domin.enums.TipoTransacao;

import java.time.LocalDate;
import java.util.Date;

public record TransacoeResponseDto(Long id, String descricao, Double valor, TipoTransacao tipo, @JsonFormat(pattern = "yyyy-MM-dd") LocalDate data, Long categoriaId, String categoriaNome,String cor){
    public TransacoeResponseDto(Transacao transacao){
        this(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getData(),
                transacao.getCategoria().getId()  != null ? transacao.getCategoria().getId() : null,
                transacao.getCategoria().getNome() !=null ? transacao.getCategoria().getNome() : "Sem categoria",
                transacao.getCategoria().getCor() != null ? transacao.getCategoria().getCor() : null
        );
    }
}
