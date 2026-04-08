/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

import java.time.LocalDateTime;

public class ConsentimentoResponseDTO {
    private LocalDateTime dataRegistro;
    private String versaoTermo;
    private boolean ativo;

    public ConsentimentoResponseDTO(LocalDateTime dataRegistro, String versaoTermo, boolean ativo) {
        this.dataRegistro = dataRegistro;
        this.versaoTermo = versaoTermo;
        this.ativo = ativo;
    }

    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
    public String getVersaoTermo() { return versaoTermo; }
    public void setVersaoTermo(String versaoTermo) { this.versaoTermo = versaoTermo; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
}
