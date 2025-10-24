package com.senac.aulaFull.application.dto.usuario;

import com.senac.aulaFull.domin.entites.Usuario;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;


public record UsuarioPrincipalDto (Long id, String email, Collection<? extends GrantedAuthority> autorizacao) {

    public UsuarioPrincipalDto(Usuario usuario) {
        this(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getAuthorities()
        );
    }
}



