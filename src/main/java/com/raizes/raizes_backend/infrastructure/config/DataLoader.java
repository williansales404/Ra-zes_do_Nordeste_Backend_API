/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.infrastructure.config;

import com.raizes.raizes_backend.domain.entity.Gerente;
import com.raizes.raizes_backend.domain.repository.ProdutoRepository;
import com.raizes.raizes_backend.domain.repository.UnidadeRepository;
import com.raizes.raizes_backend.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner{
    @Autowired private UsuarioRepository usuarioRepo;
    @Autowired private UnidadeRepository unidadeRepo;
    @Autowired private ProdutoRepository produtoRepo;
    //@Autowired private PasswordEncoder passwordEncoder;
    
     @Override
    public void run(String... args) throws Exception {
        // Criar um gerente admin
        // Criar uma unidade exemplo
        // Criar alguns produtos ("Baião de Dois", "Tapioca", "Cuscuz")
    }
}
