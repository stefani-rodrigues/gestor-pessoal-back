package com.senac.aulaFull.application.dto.usuario;

public record UsuarioRequestDto(String nome,
                                String email,
                                String senha,
                                String CPF,
                                String role) {



}
