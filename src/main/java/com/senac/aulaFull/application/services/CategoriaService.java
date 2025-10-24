package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.dto.categoria.CategoriaRequest;
import com.senac.aulaFull.application.dto.categoria.CategoriaResponse;
import com.senac.aulaFull.domin.entites.Categoria;
import com.senac.aulaFull.domin.repository.CategoriaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    private CategoriaRepository categoriaRepository;

    public Categoria CriarNovaCategoria(CategoriaRequest categoriaRequest){
        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome(categoriaRequest.nome());
        novaCategoria.setCor(categoriaRequest.cor());

        return categoriaRepository.save(novaCategoria);
    }

    public CategoriaResponse EditarCategoria(Long id , CategoriaRequest categoriaRequest){
        Categoria categoriaExistente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoriaExistente.setCor(categoriaRequest.cor());
        categoriaExistente.setNome(categoriaRequest.nome());

        Categoria  categoriaAtual = categoriaRepository.save(categoriaExistente);

        return toResponseDto(categoriaAtual);
    }

    public List<CategoriaResponse> ListarTodasCategorias(){
        return categoriaRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    public void ExcluirCategoria(Long id){

        if (!categoriaRepository.existsById(id)){
            throw  new RuntimeException("Categoria não encontrada");
        }
        categoriaRepository.deleteById(id);
    }

    public CategoriaResponse toResponseDto (Categoria categoria){
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getCor(),
                categoria.getNome()
        );
    }
}

