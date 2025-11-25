package com.senac.aulaFull.application.services;
import com.senac.aulaFull.application.dto.transacao.TransacoeRequestDto;
import com.senac.aulaFull.application.dto.transacao.TransacoeResponseDto;
import com.senac.aulaFull.application.dto.usuario.UsuarioPrincipalDto;
import com.senac.aulaFull.domin.entites.Categoria;
import com.senac.aulaFull.domin.entites.Transacao;
import com.senac.aulaFull.domin.entites.Usuario;
import com.senac.aulaFull.domin.enums.TipoTransacao;
import com.senac.aulaFull.domin.interfaces.SomaPorTipoResponse;
import com.senac.aulaFull.domin.repository.CategoriaRepository;
import com.senac.aulaFull.domin.repository.TransacaoRepository;
import com.senac.aulaFull.domin.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@Service
public class TransacaoService {

    @Autowired
    private TransacaoRepository transacaoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Transacao CriarNovaTransacao(TransacoeRequestDto transacaoRequest,Usuario usuario) {


        if (transacaoRequest.descricao() == null || transacaoRequest.descricao().isBlank()) {
            throw new IllegalArgumentException("A descrição não pode estar vazia");
        }
        if (transacaoRequest.valor() == null || transacaoRequest.valor() <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }
        if (transacaoRequest.data() == null) {
            throw new IllegalArgumentException("A data não pode ser nula");
        }

        Categoria categoria = categoriaRepository.findById(transacaoRequest.categoriaId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada"));

        Transacao novaTransacao = new Transacao();
        novaTransacao.setDescricao(transacaoRequest.descricao());
        novaTransacao.setValor(transacaoRequest.valor());
        novaTransacao.setData(transacaoRequest.data());
        novaTransacao.setTipo(transacaoRequest.tipo());
        novaTransacao.setCategoria(categoria);
        novaTransacao.setUsuario(usuario);

        return transacaoRepository.save(novaTransacao);
    }

    public TransacoeResponseDto AtualizarTransacao(Long id, TransacoeRequestDto request,Usuario usuario){

        Transacao transacaoExistente = transacaoRepository.findByIdAndUsuario(id,usuario)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        var categoria = categoriaRepository.findById(request.categoriaId())
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada"));

            transacaoExistente.setDescricao(request.descricao());
            transacaoExistente.setValor(request.valor());
            transacaoExistente.setData(request.data());
            transacaoExistente.setTipo(request.tipo());
            transacaoExistente.setCategoria(categoria);

        Transacao  transacaoAtual = transacaoRepository.save(transacaoExistente);
        return new TransacoeResponseDto(transacaoAtual);
    }

    public List<TransacoeResponseDto> ListarTodasTransacoes(Usuario usuario){
        return transacaoRepository.findByUsuario(usuario)
                .stream()
                .map(TransacoeResponseDto::new)
                .toList();
    }

    public List<TransacoeResponseDto> ListarPorTransacoesRecente(Usuario usuario){
        LocalDate dataAtual = LocalDateTime.now().toLocalDate();
        LocalDate inicioDaSemana =  dataAtual.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        LocalDate fimDaSemana = dataAtual.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

        List<Transacao> transacoes = transacaoRepository.findByUsuarioAndDataBetween(usuario, inicioDaSemana, fimDaSemana);

        return transacoes.stream()
                .map(TransacoeResponseDto::new)
                .toList();
    }

    public Double ObterTotalPorTipoNoMes(TipoTransacao tipo, int mes, int ano,Usuario usuario) {
        return transacaoRepository.buscarTotalPorTipo(usuario,tipo,mes, ano);
    }


    public List<TransacoeResponseDto> ListarTransacoesPorMes(Integer mes , Integer ano,Usuario usuario, TipoTransacao tipo) {
        List<Transacao> lista = transacaoRepository.findByUsuarioAndMesAno(usuario,mes,ano, tipo);
        return lista.stream()
                .map(TransacoeResponseDto::new)
                .toList();
    }

    public TransacoeResponseDto ConsultarPorId(Long id,Usuario usuario ){
        return transacaoRepository.findByIdAndUsuario(id,usuario)
                .map(TransacoeResponseDto::new)
                .orElse(null);
    }

   public List<SomaPorTipoResponse> ObterResumoTotalPorTipoDoMes(TipoTransacao tipo, int mes, int ano,Usuario usuario){
        return transacaoRepository.buscarSomaPorTipoMes(tipo, mes, ano, usuario);
   }


    public void DeletarTransacao (Long id,Usuario usuario) {
        Transacao transacao = transacaoRepository.findByIdAndUsuario(id,usuario).orElseThrow(() -> new RuntimeException("Transação não encontrada ou acesso negado"));

        transacaoRepository.deleteById(transacao.getId());
    }

}
