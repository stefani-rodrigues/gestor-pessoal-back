package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.dto.categoria.CategoriaRequestDto;
import com.senac.aulaFull.application.dto.categoria.CategoriaResponseDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.aulaFull.domin.entites.Categoria;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.repository.CategoriaRepository;
import com.senac.aulaFull.domin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    public Categoria CriarNovaCategoria(CategoriaRequestDto categoriaRequest){

        Usuario usuario = getUsuarioLogado();

        if (categoriaRequest.nome() == null || categoriaRequest.nome().isBlank()) {
            throw new IllegalArgumentException("Categoria não pode estar vazia");
        }

        Categoria novaCategoria = new Categoria();
        novaCategoria.setNome(categoriaRequest.nome());
        novaCategoria.setCor(categoriaRequest.cor());
        novaCategoria.setUsuario(usuario);

        return categoriaRepository.save(novaCategoria);
    }


    private Usuario getUsuarioLogado(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        String email;

        if (principal instanceof UsuarioPrincipalDto usuarioDto) {
            email = usuarioDto.email();
        } else {
            email = authentication.getName();
        }

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado para o email: " + email));
    }


    public CategoriaResponseDto EditarCategoria(Long id , CategoriaRequestDto categoriaRequest){

        Usuario usuario = getUsuarioLogado();

        Categoria categoriaExistente = categoriaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

        categoriaExistente.setNome(categoriaRequest.nome());
        categoriaExistente.setCor(categoriaRequest.cor());

        Categoria categoriaAtualizada = categoriaRepository.save(categoriaExistente);

        return new CategoriaResponseDto(categoriaAtualizada);
    }

    public CategoriaResponseDto RecuperarCategoria(Long id){
        Usuario usuario = getUsuarioLogado();

        CategoriaResponseDto categoriaExistente = categoriaRepository
                .findByIdAndUsuarioId(id, usuario.getId())
                .map(CategoriaResponseDto::new)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada "));

        return categoriaExistente;
    }

    public List<CategoriaResponseDto> ListarTodasCategorias(){

        Usuario usuario = getUsuarioLogado();

        return categoriaRepository.findAllByUsuarioId(usuario.getId())
                .stream()
                .map(CategoriaResponseDto::new)
                .toList();
    }


    public ResponseEntity<?> ExcluirCategoria(Long id){
        Usuario usuario = getUsuarioLogado();

        Categoria categoria = categoriaRepository.findByIdAndUsuarioId(id,usuario.getId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada para este usuário"));
        try {

            categoriaRepository.delete(categoria);

            return ResponseEntity.ok("Categoria Deletada com sucesso!");

        } catch (Exception e) {

            Throwable cause = e.getCause();
            while (cause != null) {
                if (cause instanceof org.hibernate.exception.ConstraintViolationException) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Map.of("erro", "Não é possível deletar a categoria, pois ela está sendo usada em transações."));
                }
                cause = cause.getCause();
            }
            String mensagem = e.getMessage();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", mensagem));
        }
    }
}
