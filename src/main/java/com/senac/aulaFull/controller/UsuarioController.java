package com.senac.aulaFull.controller;

import com.senac.aulaFull.dto.usuario.UsuarioRequest;
import com.senac.aulaFull.dto.usuario.UsuarioResponseDto;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
@Tag(name= "Controlador de usuarios", description = "Camada responsável por controlar os registros de usuarios")
public class UsuarioController {

   @Autowired
   private UsuarioService usuarioService;

    @PostMapping
    @Operation(summary = "Criar um novo usuário",description = "Método responsável para a  criação do usuário")
    public ResponseEntity<UsuarioResponseDto>CriarNovoUsuario(@RequestBody UsuarioRequest usuarioRequest){

        try {
            Usuario usuarioSalvo =usuarioService.criarUsuario(usuarioRequest);

            UsuarioResponseDto responseDto = new UsuarioResponseDto(
                    usuarioSalvo.getId(),
                    usuarioSalvo.getNome(),
                    usuarioSalvo.getEmail(),
                    usuarioSalvo.getRole()
            );

            return  new ResponseEntity<>(responseDto, HttpStatus.CREATED);
        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Método responsável por listar todos os usuários")
    public ResponseEntity<List<UsuarioResponseDto>> consultarTodosUsuario(){
        List<UsuarioResponseDto> usuarios = usuarioService.listarTodosUsuarios();
        return ResponseEntity.ok(usuarios);
    }




    @GetMapping("/{id}")
    @Operation(summary = "Buscar usuario por id",description = "Método responsável por buscar o usuário por id")
    public ResponseEntity<UsuarioResponseDto> buscarPorId (@PathVariable Long id){
         UsuarioResponseDto usuarioResponseDto = usuarioService.buscarPorId(id);

         if (usuarioResponseDto == null){
                return ResponseEntity.notFound().build();
         }
       return ResponseEntity.ok(usuarioResponseDto);
    }




}
