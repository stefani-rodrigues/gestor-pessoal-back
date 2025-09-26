package com.senac.aulaFull.services;

import com.senac.aulaFull.dto.transacao.SaldoDto;
import com.senac.aulaFull.dto.transacao.TransacoeRequest;
import com.senac.aulaFull.dto.transacao.TransacoeResponse;
import com.senac.aulaFull.model.Transacao;
import com.senac.aulaFull.repository.TransacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    public Transacao criarNovaTransacao(TransacoeRequest transacaoRequest){
        Transacao novaTransacao = new Transacao();
        novaTransacao.setDescricao(transacaoRequest.descricao());
        novaTransacao.setValor(transacaoRequest.valor());
        novaTransacao.setData(transacaoRequest.data());
        novaTransacao.setTipo(transacaoRequest.tipo());

       return transacaoRepository.save(novaTransacao);
    }

    public TransacoeResponse AtualizarTransacao(Long id, TransacoeRequest request){
        Transacao transacaoExistente = transacaoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

//        transacaoExistente.setDescricao(request.descricao());
        transacaoExistente.setValor(request.valor());
        transacaoExistente.setData(request.data());
        transacaoExistente.setTipo(request.tipo());

        Transacao  transacaoAtual = transacaoRepository.save(transacaoExistente);

        return  toResponseDto(transacaoAtual);
    }

    public List<TransacoeResponse> listarTodasTransacao(){
        return transacaoRepository.findAll()
                .stream()
                .map(this::toResponseDto)
                .toList();

    }

    public SaldoDto SaldoAtual(Long usuarioId){

        Double totalReceitas = transacaoRepository.sumReceitasByUsuarioId(usuarioId);
        Double totalDespesa = transacaoRepository.sumDespesasByUsuarioId(usuarioId);
        Double totalInvestimento = transacaoRepository.sumInvestimentoByUsuarioId(usuarioId);

        Double saldo = totalReceitas - totalDespesa - totalInvestimento;

        return new SaldoDto(saldo);
    }

    public Double TotalInvestido (Long usuarioId){
        return transacaoRepository.sumInvestimentoByUsuarioId(usuarioId);
    }

    public void DeletarTransacao (Long id) {
        if (!transacaoRepository.existsById(id)){
            throw new RuntimeException("Transação não encontrada");
        }
        transacaoRepository.deleteById(id);
    }

    private TransacoeResponse toResponseDto(Transacao transacao) {
        return new TransacoeResponse(
                transacao.getId(),
                transacao.getDescricao(),
                transacao.getValor(),
                transacao.getTipo(),
                transacao.getData()
        );
}  }

