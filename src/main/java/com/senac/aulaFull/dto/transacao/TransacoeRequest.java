package com.senac.aulaFull.dto.transacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senac.aulaFull.utis.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Date;

public record TransacoeRequest(
        @NotBlank
        String descricao,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        Date data,

        @NotNull
        TipoTransacao tipo,

        @NotNull
        @Positive
        Double valor
) {
}
