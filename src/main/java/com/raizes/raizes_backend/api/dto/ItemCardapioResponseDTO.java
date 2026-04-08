/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

public class ItemCardapioResponseDTO {
    private Long id;
    private Long produtoId;
    private String produtoNome;
    private Double preco;
    private Boolean disponivel;

    public ItemCardapioResponseDTO(Long id, Long produtoId, String produtoNome, Double preco, Boolean disponivel) {
        this.id = id;
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.preco = preco;
        this.disponivel = disponivel;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public String getProdutoNome() { return produtoNome; }
    public void setProdutoNome(String produtoNome) { this.produtoNome = produtoNome; }
    public Double getPreco() { return preco; }
    public void setPreco(Double preco) { this.preco = preco; }
    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
}