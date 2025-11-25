package com.senac.aulaFull.presentation;

import com.senac.aulaFull.application.dto.usuario.AlterarPerfilUsuarioDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioRequestDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioResponseDto;
import com.senac.aulaFull.application.services.TokenService;
import com.senac.aulaFull.application.services.UsuarioService;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

   @Autowired
   private TokenService tokenService;

   @Autowired
   private UsuarioRepository usuarioRepository;


    @PostMapping
    @Operation(summary = "Criar novo Usuário", description = "Método resposavel em criar usuarios!")
    public ResponseEntity<UsuarioResponseDto> criarUsuario(@RequestBody UsuarioRequestDto usuario){

        try {

            var usuarioResponse = usuarioService.CriarUsuario(usuario);

            return ResponseEntity.ok(usuarioResponse);

        }catch (Exception e) {
            String mensagem = e.getMessage();
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários", description = "Método responsável por listar todos os usuários")
    public ResponseEntity<List<UsuarioResponseDto>> listarTodosUsuario(){
         return ResponseEntity.ok(usuarioService.ListarTodosUsuarios());
    }


    @GetMapping("/{id}")
    @Operation(summary = "Consultar usuario por id",description = "Método responsável por buscar o usuário por id")
    public ResponseEntity<UsuarioResponseDto> consultarPorId(@PathVariable Long id){

         var usuario = usuarioService.ConsultarPorId(id);

        SecurityContextHolder.getContext().getAuthentication().getPrincipal();

         if (usuario == null){
                return ResponseEntity.notFound().build();
         }
       return ResponseEntity.ok(usuario);

    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar perfil do usuario",description = "Atualiza perfil do usuário logado")
    public ResponseEntity<AlterarPerfilUsuarioDto> atualizarPerfil(@PathVariable Long id, @Valid @RequestBody AlterarPerfilUsuarioDto request) {
        AlterarPerfilUsuarioDto perfilAtualizado = usuarioService.AtualizarPerfil(id,request);

        return ResponseEntity.ok(perfilAtualizado);
    }

    private Usuario getUsuarioLogado(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        var usuarioPrincipal = tokenService.ValidarToken(token);
        return usuarioRepository.findById(usuarioPrincipal.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

}
