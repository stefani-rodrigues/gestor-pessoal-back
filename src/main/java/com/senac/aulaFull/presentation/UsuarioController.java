package com.senac.aulaFull.presentation;

import com.senac.aulaFull.application.dto.usuario.UsuarioRequestDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioResponseDto;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name= "Controlador de usuarios", description = "Camada responsável por controlar os registros de usuarios")
public class UsuarioController {

   @Autowired
   private UsuarioService usuarioService;


    @PostMapping
    @Operation(summary = "Salvar Usuário", description = "Método resposavel em criar usuarios!")
    public ResponseEntity<UsuarioResponseDto> salvarUsuario(@RequestBody UsuarioRequestDto usuario){

        try {

            var usuarioResponse = usuarioService.salvarUsuario(usuario);

            return ResponseEntity.ok(usuarioResponse);

        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Método responsável por listar todos os usuários")
    public ResponseEntity<List<UsuarioResponseDto>> listarTodosUsuario(){
         return ResponseEntity.ok(usuarioService.listarTodosUsuarios());
    }




    @GetMapping("/{id}")
    @Operation(summary = "Consultar usuario por id",description = "Método responsável por buscar o usuário por id")
    public ResponseEntity<UsuarioResponseDto> consultarPorId(@PathVariable Long id){

         var usuario = usuarioService.consultarPorId(id);

         //Busca todos os usuarios logado
        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

         if (usuario == null){
                return ResponseEntity.notFound().build();
         }
       return ResponseEntity.ok(usuario);
    }




}
