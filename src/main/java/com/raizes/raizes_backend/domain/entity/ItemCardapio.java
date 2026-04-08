/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.domain.entity;

import jakarta.persistence.*;

@Entity
public class ItemCardapio {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double precoPersonalizado;
    private Boolean disponivel = true;

    @ManyToOne
    private Produto produto;

    @ManyToOne
    private Cardapio cardapio;

    public ItemCardapio() {}
    public ItemCardapio(Produto produto, Cardapio cardapio, Double precoPersonalizado) {
        this.produto = produto;
        this.cardapio = cardapio;
        this.precoPersonalizado = precoPersonalizado;
        this.disponivel = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Double getPrecoPersonalizado() { return precoPersonalizado; }
    public void setPrecoPersonalizado(Double precoPersonalizado) { this.precoPersonalizado = precoPersonalizado; }
    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public Cardapio getCardapio() { return cardapio; }
    public void setCardapio(Cardapio cardapio) { this.cardapio = cardapio; }
}