package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.dto.login.LoginRequestDto;
import com.senac.aulaFull.application.dto.login.LoginResponseDto;
import com.senac.aulaFull.application.dto.usuario.*;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.interfaces.IEnvioEmail;
import com.senac.aulaFull.domin.repository.UsuarioRepository;
import com.senac.aulaFull.application.dto.login.EsqueciMinhaSenhaDto;
import com.senac.aulaFull.infra.external.EnvioEmailRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;
;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private  UsuarioRepository usuarioRepository;

    @Autowired
    private IEnvioEmail iEnvioEmail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenService tokenService;


    @Autowired
    private EnvioEmailRepository IEnvioEmail;


    @Transactional
    public UsuarioResponseDto CriarUsuario (UsuarioRequestDto usuarioRequest) {

        if (usuarioRepository.findByEmail(usuarioRequest.email()).isPresent()) {
            throw new RuntimeException("Email já cadastrado.");
        }

        if (usuarioRepository.findByCpf(usuarioRequest.CPF()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado.");
        }

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioRequest.nome());
        novoUsuario.setEmail(usuarioRequest.email());
        novoUsuario.setCpf(usuarioRequest.CPF());
        novoUsuario.setSenha(passwordEncoder.encode(usuarioRequest.senha()));
        novoUsuario.setRole(usuarioRequest.role());

        Usuario salvo = usuarioRepository.save(novoUsuario);

        return new UsuarioResponseDto(salvo);
    }


    public LoginResponseDto ValidarSenha(LoginRequestDto login) {
        Usuario usuario = usuarioRepository.findByEmail(login.email())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!passwordEncoder.matches(login.senha(), usuario.getSenha())) {
            throw new RuntimeException("Senha inválida");
        }

        String token = tokenService.GerarToken(usuario);
        return new LoginResponseDto(
                token,
                usuario.getId(),
                usuario.getEmail(),
                usuario.getNome()
        );
    }


    public UsuarioResponseDto ConsultarPorId(Long id){

        return  usuarioRepository.findById(id)
                .map(UsuarioResponseDto::new)
                .orElse(null);

    }

    public List<UsuarioResponseDto> ListarTodosUsuarios(){
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDto::new)
                .toList();
    }

    private UsuarioResponseDto toDtoResponse(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole(),
                usuario.getTelefone(),
                usuario.getDataNascimento(),
                usuario.getGenero()
        );
    }

    public AlterarPerfilUsuarioDto AtualizarPerfil(Long id, AlterarPerfilUsuarioDto request) {
        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (request.nome() != null && !request.nome().isBlank()) {
            usuarioExistente.setNome(request.nome());
        }
        if (request.telefone() != null && !request.telefone().isBlank()) {
            usuarioExistente.setTelefone(request.telefone());
        }
        if (request.genero() != null && !request.genero().isBlank()) {
            usuarioExistente.setGenero(request.genero());
        }
        if (request.CPF() != null && !request.CPF().isBlank()) {
            usuarioExistente.setCpf(request.CPF());
        }

        if (request.dataNascimento() != null) {
            usuarioExistente.setDataNascimento(request.dataNascimento());
        }
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
        return new AlterarPerfilUsuarioDto(usuarioAtualizado);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com esse email"+ username));
    }

    public void RecuperarSenhaEnvio(UsuarioPrincipalDto usuarioLogado) {

        iEnvioEmail.enviarEmailSimples("stefani.deoliveira19@gmail.com","Código Recuperado", GerarCodigoAleatorio(8));


    }

    public String GerarCodigoAleatorio(int length) {

        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            senha.append(CHARS.charAt(randomIndex));
        }
        return senha.toString();
    }

    public void EsqueciMinhaSenha(EsqueciMinhaSenhaDto esqueciMinhaSenhaDto) {

        var usuario = usuarioRepository.findByEmail(esqueciMinhaSenhaDto.email())
                .orElseThrow(() -> new RuntimeException("Email não encontrado"));

        String codigo = GerarCodigoAleatorio(6);

        usuario.setTokenSenha(codigo);
        usuario.setTokenExpira(LocalDateTime.now().plusMinutes(10));
        usuarioRepository.save(usuario);

        IEnvioEmail.enviarEmailComTemplate(
                usuario.getEmail(),
                "🔐 Seu código para redefinir senha",
                codigo
        );
    }

    public  void RegistrarNovaSenha(RegistrarNovaSenhaDto registrarNovaSenhaDto){

        var usuario = usuarioRepository
                .findByEmailAndTokenSenha(registrarNovaSenhaDto.email(),
                                            registrarNovaSenhaDto.token())
                                            .orElse(null);
        if (usuario!= null){
            usuario.setSenha(passwordEncoder.encode(registrarNovaSenhaDto.senha()));
            usuarioRepository.save(usuario);
        }
    }


}


