package com.senac.aulaFull.application.dto.usuario;

import com.senac.aulaFull.domin.entites.Usuario;

public record UsuarioResponseDto(Long id, String nome, String email, String CPF, String role){

    public UsuarioResponseDto (Usuario usuario){
        this(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getRole()
        );
    }
}
