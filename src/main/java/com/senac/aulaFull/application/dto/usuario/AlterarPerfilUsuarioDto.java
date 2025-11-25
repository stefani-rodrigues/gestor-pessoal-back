package com.senac.aulaFull.application.dto.usuario;

import com.senac.aulaFull.domin.entites.Usuario;

import java.time.LocalDate;

public record AlterarPerfilUsuarioDto (
        String nome,
        String genero,
        LocalDate dataNascimento,
        String  telefone,
        String CPF){

    public AlterarPerfilUsuarioDto (Usuario usuario){
        this(
                usuario.getNome(),
                usuario.getGenero(),
                usuario.getDataNascimento(),
                usuario.getTelefone(),
                usuario.getCpf()
        );
    }
}
