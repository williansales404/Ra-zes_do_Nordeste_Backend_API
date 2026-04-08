/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

public class UnidadeResponseDTO {
    private Long id;
    private String nome;
    private String endereco;
    private String horarioFuncionamento;
    private boolean ativa;
    private String nomeGerente;

    public UnidadeResponseDTO(Long id, String nome, String endereco, String horarioFuncionamento, boolean ativa, String nomeGerente) {
        this.id = id;
        this.nome = nome;
        this.endereco = endereco;
        this.horarioFuncionamento = horarioFuncionamento;
        this.ativa = ativa;
        this.nomeGerente = nomeGerente;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public String getHorarioFuncionamento() { return horarioFuncionamento; }
    public void setHorarioFuncionamento(String horarioFuncionamento) { this.horarioFuncionamento = horarioFuncionamento; }
    public boolean isAtiva() { return ativa; }
    public void setAtiva(boolean ativa) { this.ativa = ativa; }
    public String getNomeGerente() { return nomeGerente; }
    public void setNomeGerente(String nomeGerente) { this.nomeGerente = nomeGerente; }
}