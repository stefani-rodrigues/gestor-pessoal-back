package com.senac.aulaFull.presentation;


import com.senac.aulaFull.application.dto.login.EsqueciMinhaSenhaDto;
import com.senac.aulaFull.application.dto.login.LoginRequestDto;
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
    @Operation(summary = "Login",description = "Metodo resposavel por efetuar o login do usuario ")
    public ResponseEntity <?> login (@RequestBody LoginRequestDto request){
        try{
            usuarioService.validarSenha(request);

            var token = tokenService.gerarToken(request);

            return ResponseEntity.ok(token);
        } catch (RuntimeException e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/recuperarSenha/envio")
    @Operation(summary = "Recuperar senha",description = "Metodo para recuperar a senha")
    public ResponseEntity<?> recuperarSenhaEnvio(@AuthenticationPrincipal UsuarioPrincipalDto usuarioLogado){

        usuarioService.recuperarSenhaEnvio(usuarioLogado);
        return ResponseEntity.ok("Codigo enviado com sucesso");

    }

    @PostMapping("/esqueciminhasenha")
    @Operation(summary = "Esqueci minha senha",description = "metodo para recuperar senha")
    public ResponseEntity<?> esqueciMinhaSenha(@RequestBody EsqueciMinhaSenhaDto esqueciMinhaSenhaDto)
    {

        try {
            usuarioService.esqueciMinhaSenha(esqueciMinhaSenhaDto);
                    return ResponseEntity.ok().build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
