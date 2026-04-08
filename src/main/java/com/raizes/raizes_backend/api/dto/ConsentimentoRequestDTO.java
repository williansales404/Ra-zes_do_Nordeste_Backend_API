/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.dto;

public class ConsentimentoRequestDTO {
    private boolean aceito;
    private String versaoTermo;

    public boolean isAceito() { return aceito; }
    public void setAceito(boolean aceito) { this.aceito = aceito; }
    public String getVersaoTermo() { return versaoTermo; }
    public void setVersaoTermo(String versaoTermo) { this.versaoTermo = versaoTermo; }
}
