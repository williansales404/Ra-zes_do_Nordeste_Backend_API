/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

public class ItemCardapioRequestDTO {
    private Long produtoId;
    private Double precoPersonalizado;
    private Boolean disponivel;

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    public Double getPrecoPersonalizado() { return precoPersonalizado; }
    public void setPrecoPersonalizado(Double precoPersonalizado) { this.precoPersonalizado = precoPersonalizado; }
    public Boolean getDisponivel() { return disponivel; }
    public void setDisponivel(Boolean disponivel) { this.disponivel = disponivel; }
}