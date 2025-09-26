package com.senac.aulaFull.controller;


import com.senac.aulaFull.dto.LoginRequestDto;
import com.senac.aulaFull.services.TokenService;
import com.senac.aulaFull.services.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
