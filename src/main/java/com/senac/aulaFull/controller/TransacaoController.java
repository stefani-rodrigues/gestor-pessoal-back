package com.senac.aulaFull.controller;

import com.senac.aulaFull.dto.transacao.SaldoDto;
import com.senac.aulaFull.dto.transacao.TransacoeRequest;
import com.senac.aulaFull.dto.transacao.TransacoeResponse;
import com.senac.aulaFull.model.Transacao;
import com.senac.aulaFull.model.Usuario;
import com.senac.aulaFull.services.TransacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/transicoes")
@Tag( name = "Controlador de transições", description = "Camadada responável por registrar as transições")
public class TransacaoController {

    @Autowired
    private TransacaoService transacaoService;


    @PostMapping
    @Operation(summary = "Cadastro nova transação",description = "Metodo responsável por criar uma nova transação")
    public ResponseEntity<TransacoeResponse> criarNovaTransacao (@Valid  @RequestBody TransacoeRequest request){
        try {
            Transacao transacaoSalva = transacaoService.criarNovaTransacao(request);

            TransacoeResponse response = new TransacoeResponse(
                    transacaoSalva.getId(),
                    transacaoSalva.getDescricao(),
                    transacaoSalva.getValor(),
                    transacaoSalva.getTipo(),
                    transacaoSalva.getData()
            );
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    @GetMapping
    @Operation(summary = "Listar todas as transação",description = "Metodo responsável por listar todas as transação")
    public ResponseEntity<List<TransacoeResponse>> consultarTodasTransacoes(){
        List<TransacoeResponse> transacoes = transacaoService.listarTodasTransacao();

        return ResponseEntity.ok(transacoes);
    }

    @GetMapping("/saldo")
    @Operation(summary = "Saldo atual do usuario",description = "Metodo responsável por retornar o saldo do usuário")
    public ResponseEntity<SaldoDto> retornarSaldoAtual (Authentication authentication){

        Usuario usuarioLogado = (Usuario) authentication.getPrincipal();

        SaldoDto saldoAtual = transacaoService.SaldoAtual(usuarioLogado.getId());

        return ResponseEntity.ok(saldoAtual);
    }



    @PutMapping("/{id}")
    @Operation(summary = "atualizar a transação",description = "Atualizar uma transação")
    public ResponseEntity<TransacoeResponse> atualizarTransacao(@PathVariable Long id,
                                                                @Valid @RequestBody TransacoeRequest request) {
          TransacoeResponse transacaoAtualizada = transacaoService.AtualizarTransacao(id,request);

        return ResponseEntity.ok(transacaoAtualizada);

    }

    @DeleteMapping
    @Operation(summary = "deletar a transação",description = "Deletar uma transação")
    public void deletarTransacao(@PathVariable Long id){
         transacaoService.DeletarTransacao(id);
    }
}
