package com.senac.aulaFull.model;

import com.senac.aulaFull.utis.TipoTransacao;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "transacoes")
public class Transacao   {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private  String descricao;
    private Date data;
    private  Double valor;

    @Enumerated(EnumType.STRING)
    private TipoTransacao tipo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuarioId", nullable = true)
    private Usuario usuario;
}
