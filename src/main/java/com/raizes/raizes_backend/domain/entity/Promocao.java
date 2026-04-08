/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Promocao {
    @Id @GeneratedValue
    private Long id;
    
    private String nome;
    private String descricao;
    private Double percentualDesconto;
    private Double valorMinimo; // pedido mínimo para aplicar
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    
    public Promocao() {}

    public Promocao(String nome, Double percentualDesconto, Double valorMinimo, LocalDateTime dataInicio, LocalDateTime dataFim) {
        this.nome = nome;
        this.percentualDesconto = percentualDesconto;
        this.valorMinimo = valorMinimo;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Double getPercentualDesconto() { return percentualDesconto; }
    public void setPercentualDesconto(Double percentualDesconto) { this.percentualDesconto = percentualDesconto; }
    public Double getValorMinimo() { return valorMinimo; }
    public void setValorMinimo(Double valorMinimo) { this.valorMinimo = valorMinimo; }
    public LocalDateTime getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDateTime dataInicio) { this.dataInicio = dataInicio; }
    public LocalDateTime getDataFim() { return dataFim; }
    public void setDataFim(LocalDateTime dataFim) { this.dataFim = dataFim; }
}
