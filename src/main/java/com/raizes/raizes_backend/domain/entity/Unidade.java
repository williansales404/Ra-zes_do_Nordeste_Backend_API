/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Unidade {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nome;
    private String endereco;
    private String horarioFuncionamento;
    private boolean ativa = true;
    
    @ManyToOne
    private Usuario gerente;

    public Unidade() {
    }

    public Unidade(Long id, String nome, String endereco, String horarioFuncionamento, Usuario gerente) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.horarioFuncionamento = horarioFuncionamento;
        this.gerente = gerente;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getHorarioFuncionamento() {
        return horarioFuncionamento;
    }

    public boolean isAtiva() {
        return ativa;
    }

    public Usuario getGerente() {
        return gerente;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setHorarioFuncionamento(String horarioFuncionamento) {
        this.horarioFuncionamento = horarioFuncionamento;
    }

    public void setAtiva(boolean ativa) {
        this.ativa = ativa;
    }

    public void setGerente(Usuario gerente) {
        this.gerente = gerente;
    }
}
