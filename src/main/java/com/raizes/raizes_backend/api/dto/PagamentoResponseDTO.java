/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

import com.raizes.raizes_backend.domain.enums.StatusPagamento;

public class PagamentoResponseDTO {
    private StatusPagamento status;
    private String metodo;

    public PagamentoResponseDTO(StatusPagamento status, String metodo) {
        this.status = status;
        this.metodo = metodo;
    }

    public StatusPagamento getStatus() { return status; }
    public void setStatus(StatusPagamento status) { this.status = status; }
    public String getMetodo() { return metodo; }
    public void setMetodo(String metodo) { this.metodo = metodo; }
}