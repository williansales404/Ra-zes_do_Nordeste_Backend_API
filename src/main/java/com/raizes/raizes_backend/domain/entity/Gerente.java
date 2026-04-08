/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.domain.entity;

import com.raizes.raizes_backend.domain.enums.TipoUsuario;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDateTime;

@Entity @DiscriminatorValue("GERENTE")
public class Gerente extends Usuario {

    public Gerente() {
        super();
    }

    public Gerente(Long id, String email, String senha, String nome, String telefone, LocalDateTime dataCadastro) {
        super(id, email, senha, nome, telefone, dataCadastro, TipoUsuario.GERENTE);
    }
    
    
}
