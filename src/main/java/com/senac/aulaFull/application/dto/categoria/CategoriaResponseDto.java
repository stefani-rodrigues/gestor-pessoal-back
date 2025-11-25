package com.senac.aulaFull.application.dto.categoria;

import com.senac.aulaFull.domin.entites.Categoria;

public record CategoriaResponseDto(Long id, String nome, String cor) {

    public  CategoriaResponseDto (Categoria categoria){
        this(
                categoria.getId(),
                categoria.getNome(),
                categoria.getCor()
        );
    }
}
