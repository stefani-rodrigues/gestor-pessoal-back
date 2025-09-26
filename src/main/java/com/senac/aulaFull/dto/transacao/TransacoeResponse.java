package com.senac.aulaFull.dto.transacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senac.aulaFull.utis.TipoTransacao;

import java.util.Date;

public record TransacoeResponse(Long id,
                                String descricao,
                                Double valor,
                                TipoTransacao tipo,

                                @JsonFormat(pattern = "yyyy-MM-dd")
                                Date data
){
}
