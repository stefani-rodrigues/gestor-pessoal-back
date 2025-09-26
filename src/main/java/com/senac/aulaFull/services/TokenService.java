package com.senac.aulaFull.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.senac.aulaFull.dto.LoginRequestDto;
import com.senac.aulaFull.model.Token;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.repository.TokenRepository;
import com.senac.aulaFull.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;


@Service
public class TokenService {

    @Value("${spring.secretkey}")
    private String secret;

    @Value("${spring.tempo_expiracao}")
    private Long tempo;

    private  String emissor = "GerenciarOrcamento";

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public String gerarToken (LoginRequestDto loginResquestDto){
        var usuario = usuarioRepository.findByEmail(loginResquestDto.email()).orElse(null);

        Algorithm algorithm = Algorithm.HMAC256(secret);

        String token = JWT.create()
                .withIssuer(emissor)
                .withSubject(usuario.getEmail())
                .withExpiresAt(this.gerarDataExpiracao())
                .sign(algorithm);

       return token;
    }

    public Usuario validarToken(String token){
        Algorithm algoritm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algoritm)
                .withIssuer(emissor)
                .build();
        var tokenResult = tokenRepository.findByToken(token).orElse(null);
        if (tokenResult == null){
            throw new IllegalArgumentException("Token inválido.");
        }
        return tokenResult.getUsuario();
    }

    private Instant gerarDataExpiracao() {
        var dataAtual = LocalDateTime.now();
        dataAtual = dataAtual.plusMinutes(tempo);

        return dataAtual.toInstant(ZoneOffset.of("-03:00"));
    }
}
