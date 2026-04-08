/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.domain.entity;

import com.raizes.raizes_backend.domain.enums.CanalPedido;
import com.raizes.raizes_backend.domain.enums.StatusPedido;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime dataCriacao;
    
    @Enumerated(EnumType.STRING)
    private StatusPedido status;
    
    @Enumerated(EnumType.STRING)
    private CanalPedido canal;
    private Double valorTotal;
    
    @ManyToOne
    private Cliente cliente;
    
    @ManyToOne
    private Unidade unidade;
    
    @OneToOne(mappedBy = "pedido", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Pagamento pagamento;
    
    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemPedido> itens = new ArrayList<>();
    
    public Pedido() {
        this.dataCriacao = LocalDateTime.now();
        this.status = StatusPedido.AGUARDANDO_PAGAMENTO;
    }

    public Pedido(CanalPedido canal, Cliente cliente, Unidade unidade) {
        this();
        this.canal = canal;
        this.cliente = cliente;
        this.unidade = unidade;
    }
    
    public void adicionarItem(Produto produto, Integer quantidade, Double precoUnitario) {
        ItemPedido item = new ItemPedido(quantidade, precoUnitario, produto, this);
        this.itens.add(item);
    }

    public Double calcularValorTotal() {
        return itens.stream()
                .mapToDouble(i -> i.getQuantidade() * i.getPrecoUnitario())
                .sum();
    }

    public void atualizarStatus(StatusPedido novoStatus) {
        this.status = novoStatus;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }
    public StatusPedido getStatus() { return status; }
    public void setStatus(StatusPedido status) { this.status = status; }
    public CanalPedido getCanal() { return canal; }
    public void setCanal(CanalPedido canal) { this.canal = canal; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Unidade getUnidade() { return unidade; }
    public void setUnidade(Unidade unidade) { this.unidade = unidade; }
    public List<ItemPedido> getItens() { return itens; }
    public void setItens(List<ItemPedido> itens) { this.itens = itens; }
    public Pagamento getPagamento() { return pagamento; }
    public void setPagamento(Pagamento pagamento) { this.pagamento = pagamento; }
}
