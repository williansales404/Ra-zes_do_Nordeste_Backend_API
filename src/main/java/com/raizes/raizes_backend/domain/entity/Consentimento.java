/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
public class Consentimento {
    @Id @GeneratedValue
    private Long id;
    private LocalDateTime dataRegistro;
    private String versaoTermo;
    private boolean ativo = true;
    
    @ManyToOne
    private Cliente cliente;
    
    public Consentimento() {}

    public Consentimento(Cliente cliente, String versaoTermo) {
        this.cliente = cliente;
        this.versaoTermo = versaoTermo;
        this.dataRegistro = LocalDateTime.now();
        this.ativo = true;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
    public String getVersaoTermo() { return versaoTermo; }
    public void setVersaoTermo(String versaoTermo) { this.versaoTermo = versaoTermo; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
}