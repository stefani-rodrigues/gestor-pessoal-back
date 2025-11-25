package com.senac.aulaFull.application.dto.transacao;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.senac.aulaFull.domin.enums.TipoTransacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;
import java.util.Date;

public record TransacoeRequestDto(

        @NotBlank
        String descricao,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate data,

        @NotNull
        TipoTransacao tipo,

        @NotNull
        @Positive
        Double valor,

        @NotNull
        Long categoriaId
) {}
