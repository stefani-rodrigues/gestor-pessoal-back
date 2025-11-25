package com.senac.aulaFull.application.dto.usuario;

import com.senac.aulaFull.domin.entites.Usuario;

import java.time.LocalDate;

public record UsuarioResponseDto(Long id,
                                 String nome,
                                 String email,
                                 String CPF,
                                 String telefone,
                                 LocalDate dataNascimento,
                                 String genero){

    public UsuarioResponseDto (Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getTelefone(),
                usuario.getDataNascimento(),
                usuario.getGenero()
        );
    }
}
