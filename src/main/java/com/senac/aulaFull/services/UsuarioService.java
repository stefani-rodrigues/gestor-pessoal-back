package com.senac.aulaFull.services;

import com.senac.aulaFull.dto.LoginRequestDto;
import com.senac.aulaFull.dto.usuario.UsuarioRequest;
import com.senac.aulaFull.dto.usuario.UsuarioResponseDto;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public Usuario criarUsuario (UsuarioRequest usuarioRequest){
        if (usuarioRepository.findByEmail(usuarioRequest.email()).isPresent()){
            throw new RuntimeException("E-mail ja cadastrado");
        }
        String senhaCriptografada = passwordEncoder.encode(usuarioRequest.senha());

        Usuario novoUsuario = new Usuario();
        novoUsuario.setNome(usuarioRequest.nome());
        novoUsuario.setSenha(usuarioRequest.senha());
        novoUsuario.setEmail(usuarioRequest.email());
        novoUsuario.setSenha(senhaCriptografada);
        novoUsuario.setRole("ADMIN");

      return usuarioRepository.save(novoUsuario);
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

    public UsuarioResponseDto buscarPorId(Long id){
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o id"+ id));
        return toResponseDto (usuario);

    }

    public List<UsuarioResponseDto> listarTodosUsuarios(){
        return usuarioRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado com esse email"+ username));
    }

    private UsuarioResponseDto toResponseDto(Usuario usuario) {
        return new UsuarioResponseDto(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getRole()
        );
    } }
