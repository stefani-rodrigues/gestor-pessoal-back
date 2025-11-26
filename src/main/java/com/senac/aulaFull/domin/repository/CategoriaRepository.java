package com.senac.aulaFull.domin.repository;

import com.senac.aulaFull.domin.entites.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria,Long> {

        Optional<Categoria> findByIdAndUsuarioId(Long id, Long usuarioId);

        List<Categoria> findAllByUsuarioId(Long usuarioId);

    }

