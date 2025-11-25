package com.senac.aulaFull.presentation;


import com.senac.aulaFull.application.dto.login.EsqueciMinhaSenhaDto;
import com.senac.aulaFull.application.dto.login.LoginRequestDto;
import com.senac.aulaFull.application.dto.usuario.RegistrarNovaSenhaDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.aulaFull.application.services.TokenService;
import com.senac.aulaFull.application.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name= "Autenticação de controller", description = "Camada responsável por autenticar o usuario ")
public class AuthController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Metodo resposavel por efetuar o login do usuario ")
    public ResponseEntity <?> login (@RequestBody LoginRequestDto request){
        try{
            var response = usuarioService.ValidarSenha(request);

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {

            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/redefinirsenha/envio")
    @Operation(summary = "Recuperar nova senha",description = "Metodo para recuperar a senha")
    public ResponseEntity<?> recuperarSenhaEnvio(@AuthenticationPrincipal UsuarioPrincipalDto usuarioLogado){

        usuarioService.RecuperarSenhaEnvio(usuarioLogado);
        return ResponseEntity.ok("Codigo enviado com sucesso");

    }
    @PostMapping("/redefinirsenha")
    @Operation(summary = "Recuperar nova senha",description = "Metodo para recuperar a senha")
    public ResponseEntity<?> recuperarSenhaEnvio(@RequestBody RegistrarNovaSenhaDto dto){

        usuarioService.RegistrarNovaSenha(dto);
        return ResponseEntity.ok("Senha atualizada com sucesso!");

    }

    @PostMapping("/esqueciminhasenha")
    @Operation(summary = "Esqueci minha senha",description = "metodo para recuperar senha")
    public ResponseEntity<?> esqueciMinhaSenha(@RequestBody EsqueciMinhaSenhaDto esqueciMinhaSenhaDto) {

        try {
            usuarioService.EsqueciMinhaSenha(esqueciMinhaSenhaDto);
                    return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/registrarnovasenha")
    @Operation(summary = "Resgistrar nova senha",description = "Método para registrar nova senha!")
    public ResponseEntity<?> registrarNovaSenha(@RequestBody RegistrarNovaSenhaDto registrarNovaSenhaDto){

        try{
            usuarioService.RegistrarNovaSenha(registrarNovaSenhaDto);
            return ResponseEntity.ok().build();

        }catch (Exception e){
            return ResponseEntity.badRequest().build();
        }
    }

}
