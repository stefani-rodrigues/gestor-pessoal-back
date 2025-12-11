package com.senac.aulaFull.presentation;
import com.senac.aulaFull.application.dto.transacao.TransacoeRequestDto;
import com.senac.aulaFull.application.dto.transacao.TransacoeResponseDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.aulaFull.application.services.TokenService;
import com.senac.aulaFull.domin.entites.Transacao;
import com.senac.aulaFull.application.services.TransacaoService;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.enums.TipoTransacao;
import com.senac.aulaFull.domin.interfaces.SomaPorTipoResponse;
import com.senac.aulaFull.domin.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transacoes")
@Tag( name = "Controlador de transações", description = "Camadada responável por registrar as transações")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @PostMapping
    @Operation(summary = "Cadastro nova transação",description = "Metodo responsável por criar uma nova transação")
    public ResponseEntity<?> criarNovaTransacao (@Valid  @RequestBody TransacoeRequestDto request,@RequestHeader("Authorization") String authHeader){
        try {
            Usuario usuario = getUsuarioLogado(authHeader);
            Transacao transacaoSalva = transacaoService.CriarNovaTransacao(request,usuario);

            TransacoeResponseDto response = new TransacoeResponseDto(
                    transacaoSalva.getId(),
                    transacaoSalva.getDescricao(),
                    transacaoSalva.getValor(),
                    transacaoSalva.getTipo(),
                    transacaoSalva.getData(),
                    transacaoSalva.getCategoria().getId(),
                    transacaoSalva.getCategoria().getNome(),
                    transacaoSalva.getCategoria().getCor()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            String mensagem =  e.getMessage();
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", mensagem));
        }

    }

    @GetMapping
    @Operation(summary = "Listar todas as transação",description = "Metodo responsável por listar todas as transação")
    public ResponseEntity<List<TransacoeResponseDto>> consultarTodasTransacoes(
            @RequestParam Integer mes,
            @RequestParam Integer ano,
            @RequestParam(required = false) TipoTransacao tipo,
            @RequestHeader("Authorization") String authHeader,
            @AuthenticationPrincipal UsuarioPrincipalDto usuarioPrincipalDto){
        Usuario usuario = getUsuarioLogado(authHeader);
        List<TransacoeResponseDto> transacoes;

        if (mes != null && ano!= null) {
            transacoes = transacaoService.ListarTransacoesPorMes(mes,ano,usuario, tipo);
        } else {
            transacoes = transacaoService.ListarTodasTransacoes(usuario);
        }

        return ResponseEntity.ok(transacoes);

    }

    @GetMapping("/semana")
    @Operation(summary = "Listar transacoes recentes ",description = "Metodo responsável por listar todas as transação recentes por semana")
    public ResponseEntity<List<TransacoeResponseDto>> consultarTransacoesRecentesPorSemana(   @RequestHeader("Authorization") String authHeader) {

        Usuario usuario = getUsuarioLogado(authHeader);
        List<TransacoeResponseDto> transacoes = transacaoService.ListarPorTransacoesRecente(usuario);

        return ResponseEntity.ok(transacoes);
    }


    @GetMapping("/{id}")
    @Operation(summary = "Consultar transação por id",description = "Método responsável por buscar o transação por id")
    public ResponseEntity<TransacoeResponseDto> consultarPorId(@PathVariable Long id,@RequestHeader("Authorization") String authHeader){

        Usuario usuario = getUsuarioLogado(authHeader);
        var transacao = transacaoService.ConsultarPorId(id,usuario);


        if (transacao == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(transacao);
    }

    @GetMapping("/totalPorTipo")
    @Operation(summary = "Valor total do tipo",description = "Metodo responsável por retornar o saldo do usuário conforme o tipo")
    public List<SomaPorTipoResponse> totalPorTipo (@RequestParam TipoTransacao tipo,@RequestParam int mes,@RequestParam int ano,@RequestHeader("Authorization") String authHeader){

        Usuario usuario = getUsuarioLogado(authHeader);
        return transacaoService.ObterResumoTotalPorTipoDoMes(tipo,mes,ano,usuario);
    }

    @GetMapping("/totalPorMes")
    @Operation(summary = "Valor total por mês",description = "Metodo responsável por retornar o saldo do usuário total por mês")
    public Double totalPorMes(@RequestParam int mes, @RequestParam int ano,@RequestHeader("Authorization") String authHeader) {

        Usuario usuario = getUsuarioLogado(authHeader);
        Double totalReceita = transacaoService.ObterTotalPorTipoNoMes(TipoTransacao.RECEITA,mes,ano,usuario);
        Double totalDespesa = transacaoService.ObterTotalPorTipoNoMes(TipoTransacao.DESPESA,mes,ano,usuario);

        return  totalReceita - totalDespesa;
    }


    @PutMapping("/{id}")
    @Operation(summary = "Atualizar a transação",description = "Atualiza uma transação do usuário logado")
    public ResponseEntity<TransacoeResponseDto> atualizarTransacao(@PathVariable Long id,
                                                                   @RequestHeader("Authorization") String authHeader,
                                                                   @Valid @RequestBody TransacoeRequestDto request) {
        Usuario usuario = getUsuarioLogado(authHeader);
          TransacoeResponseDto transacaoAtualizada = transacaoService.AtualizarTransacao(id,request,usuario);

        return ResponseEntity.ok(transacaoAtualizada);

    }

    @DeleteMapping("{id}")
    @Operation(summary = "Deletar a transação",description = "Deleta uma transação do usuário logado")
    public void deletarTransacao(@PathVariable Long id,@RequestHeader("Authorization") String authHeader){
        Usuario usuario = getUsuarioLogado(authHeader);
        transacaoService.DeletarTransacao(id,usuario);
    }

    private Usuario getUsuarioLogado(String authHeader) {
        String token = authHeader.replace("Bearer ", "");
        var usuarioPrincipal = tokenService.ValidarToken(token);
        return usuarioRepository.findById(usuarioPrincipal.id())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }
}
