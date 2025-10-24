package com.senac.aulaFull.domin.repository;

import com.senac.aulaFull.domin.entites.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario,Long>  {

    Optional<Usuario> findByEmailAndNome(String email,String nome);

    Optional<Usuario> findByCpf(String CPF);

    boolean existsUsuarioByEmailContainingAndSenha(String email,String senha);

    Optional<Usuario> findByEmail(String email);
}
