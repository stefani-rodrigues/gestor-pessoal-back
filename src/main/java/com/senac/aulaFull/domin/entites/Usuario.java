package com.senac.aulaFull.domin.entites;

import com.senac.aulaFull.application.dto.usuario.UsuarioRequestDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@Table(name = "usuarios")
@NoArgsConstructor
public class Usuario implements UserDetails {


    public Usuario(UsuarioRequestDto usuarioRequestDto){
        this.setCpf(usuarioRequestDto.CPF());
        this.setNome(usuarioRequestDto.nome());
        this.setEmail(usuarioRequestDto.email());
        this.setRole(usuarioRequestDto.role());

        if (this.getDataCadastro() == null){
            this.setDataCadastro(LocalDateTime.now());
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String cpf;
    private String senha;
    private String email;
    private String role;
    private String telefone;
    private LocalDate dataNascimento;
    private String genero;

    private LocalDateTime dataCadastro;

    private String tokenSenha;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if ("ROLE_ADMIN".equals(this.role)) {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"),
                    new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else {
            return List.of(new SimpleGrantedAuthority("ROLE_USER"));
        }
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }


    public void setTokenExpira(LocalDateTime localDateTime) {
    }
}
