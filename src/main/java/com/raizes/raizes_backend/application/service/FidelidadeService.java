/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.entity.Cliente;
import com.raizes.raizes_backend.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FidelidadeService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private UsuarioService usuarioService;

    public Integer consultarPontos(Long usuarioId) {
        Cliente cliente = (Cliente) usuarioService.buscarPorId(usuarioId);
        return cliente.getPontos();
    }

    @Transactional
    public Double resgatarPontos(Long usuarioId, Integer pontosParaResgatar) {
        Cliente cliente = (Cliente) usuarioService.buscarPorId(usuarioId);
        
        if (cliente.getPontos() < pontosParaResgatar) {
            throw new RuntimeException("Pontos insuficientes");
        }
        
        Double desconto = pontosParaResgatar * 0.10;
        cliente.setPontos(cliente.getPontos() - pontosParaResgatar);
        usuarioRepository.save(cliente);
        return desconto;
    }
    
    @Transactional
    public void acumularPontos(Long clienteId, Integer pontos) {
        Cliente cliente = (Cliente) usuarioService.buscarPorId(clienteId);
        cliente.setPontos(cliente.getPontos() + pontos);
        usuarioRepository.save(cliente);
    }
}