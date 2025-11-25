package com.senac.aulaFull.presentation;
import com.senac.aulaFull.application.dto.categoria.CategoriaRequestDto;
import com.senac.aulaFull.application.dto.categoria.CategoriaResponseDto;
import com.senac.aulaFull.application.services.CategoriaService;
import com.senac.aulaFull.domin.entites.Categoria;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categoria")
@Tag(name = "Controlar as categorias",description = "Metodo responsavel pela a  categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @PostMapping
    @Operation(summary = "Cadastrar nova categoria",description = "Metodo responsável por registrar")
   public ResponseEntity <?> criarNovaCategoria (@RequestBody CategoriaRequestDto requestDto){
      try {
          Categoria categoriaSalva = categoriaService.CriarNovaCategoria(requestDto);

          CategoriaResponseDto response = new CategoriaResponseDto(
                  categoriaSalva.getId(),
                  categoriaSalva.getNome(),
                  categoriaSalva.getCor()
          );
          return new ResponseEntity<>(response, HttpStatus.CREATED);
      } catch (Exception e) {
          String mensagem = e.getMessage();
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro",mensagem));
      }
  }

   @GetMapping
   @Operation(summary = "Listar todas as categorias do usuário",description = "Metodo responsável por listar")
    public ResponseEntity<List<CategoriaResponseDto>> listarTodasCategoria(){
      List<CategoriaResponseDto>categorias = categoriaService.ListarTodasCategorias();

      return ResponseEntity.ok(categorias);
   }

    @GetMapping("/{id}")
    @Operation(summary = "recuperar a categoria pelo ID",description = "Metodo responsável por recuperar uma categoria")
    public ResponseEntity<CategoriaResponseDto> recuperarCategoria(@PathVariable Long id){
        CategoriaResponseDto categoria = categoriaService.RecuperarCategoria(id);

        return ResponseEntity.ok(categoria);
    }

   @PutMapping ("/{id}")
   @Operation(summary = "Atualizar a categoria por ID",description = "Metodo responsável por atualizar")
   public ResponseEntity<CategoriaResponseDto> atualizarCategoria(@PathVariable Long id, @Valid @RequestBody CategoriaRequestDto request){

      CategoriaResponseDto categoriaAtualizada = categoriaService.EditarCategoria(id,request);

      return ResponseEntity.ok(categoriaAtualizada);

   }

   @DeleteMapping("{id}")
   @Operation(summary = "Deletar categoria",description = "Metodo responsável por deletar uma categoria")
    public ResponseEntity<?>   deletarCategoria (@PathVariable Long id){

        return categoriaService.ExcluirCategoria(id);
   }
}
