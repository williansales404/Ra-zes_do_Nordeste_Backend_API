/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

import com.raizes.raizes_backend.domain.enums.StatusPedido;
import java.time.LocalDateTime;
import java.util.List;

public class PedidoResponseDTO {
    private Long id;
    private StatusPedido status;
    private Double valorTotal;
    private LocalDateTime dataCriacao;
    private String canal;
    private List<ItemPedidoResponseDTO> itens;

    public PedidoResponseDTO(Long id, StatusPedido status, Double valorTotal, LocalDateTime dataCriacao, String canal, List<ItemPedidoResponseDTO> itens) {
        this.id = id;
        this.status = status;
        this.valorTotal = valorTotal;
        this.dataCriacao = dataCriacao;
        this.canal = canal;
        this.itens = itens;
    }
    // getters e setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public String getCanal() { return canal; }
    public void setCanal(String canal) { this.canal = canal; }
    public List<ItemPedidoResponseDTO> getItens() { return itens; }
    public void setItens(List<ItemPedidoResponseDTO> itens) { this.itens = itens; }
}