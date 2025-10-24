package com.senac.aulaFull.application.services;

import com.senac.aulaFull.application.dto.login.LoginRequestDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioRequestDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioResponseDto;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.interfaces.IEnvioEmail;
import com.senac.aulaFull.domin.repository.UsuarioRepository;
import com.senac.aulaFull.application.dto.login.EsqueciMinhaSenhaDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private IEnvioEmail iEnvioEmail;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UsuarioResponseDto salvarUsuario(UsuarioRequestDto usuarioRequest) {

        var usuario = usuarioRepository.findByCpf(usuarioRequest.CPF())
                .map(u -> {
                    u.setNome(usuarioRequest.nome());
                    u.setSenha(usuarioRequest.senha());
                    u.setRole(usuarioRequest.role());
                    u.setEmail(usuarioRequest.email());
                    return u;
                })
                .orElse(new Usuario(usuarioRequest));

        usuarioRepository.save(usuario);

        return usuario.toDtoResponse();
    }

    public void validarSenha(LoginRequestDto login){
        boolean senhaValida = false;

        Optional<Usuario> user = usuarioRepository.findByEmail(login.email());

        if(user.isPresent()){
            senhaValida = passwordEncoder.matches(login.senha(), user.get().getSenha());
        }

        if(!senhaValida){
            throw new RuntimeException("Usuario não encontrado");
        }
    }

    public UsuarioResponseDto consultarPorId(Long id){
        return  usuarioRepository.findById(id)
                .map(UsuarioResponseDto::new)
                .orElse(null);

    }

    public List<UsuarioResponseDto> listarTodosUsuarios(){
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioResponseDto::new)
                .toList();
    }



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com esse email"+ username));
    }

    public void recuperarSenhaEnvio(UsuarioPrincipalDto usuarioLogado) {

        iEnvioEmail.enviarEmailSimples("stefani.deoliveira19@gmail.com","Código Recuperado",gerarCodigoAleatorio(8));


    }
    public String gerarCodigoAleatorio(int length) {

        final String CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder senha = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARS.length());
            senha.append(CHARS.charAt(randomIndex));
        }
        return senha.toString();
    }

    public void esqueciMinhaSenha(EsqueciMinhaSenhaDto esqueciMinhaSenhaDto) {

        var usuario = usuarioRepository.findByEmail(esqueciMinhaSenhaDto.email()).orElse(null);
        if(usuario != null) {
            var codigo = gerarCodigoAleatorio(8);

            usuario.setTokenSenha(codigo);

            usuarioRepository.save(usuario);

            iEnvioEmail.enviarEmailSimples(esqueciMinhaSenhaDto.email(),
                    "Código Recuperacao",
                    codigo
            );
        }
        }



    }


