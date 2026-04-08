/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.entity.Cliente;
import com.raizes.raizes_backend.domain.entity.Consentimento;
import com.raizes.raizes_backend.domain.repository.ConsentimentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsentimentoService {

    @Autowired
    private ConsentimentoRepository consentimentoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Transactional
    public Consentimento registrarConsentimento(Long usuarioId, String versaoTermo) {
        Cliente cliente = (Cliente) usuarioService.buscarPorId(usuarioId);
        
        // Desativa consentimentos anteriores
        consentimentoRepository.findByClienteAndAtivoTrue(cliente)
                .ifPresent(cons -> cons.setAtivo(false));
        
        Consentimento novo = new Consentimento(cliente, versaoTermo);
        return consentimentoRepository.save(novo);
    }

    public Consentimento obterConsentimentoAtivo(Long usuarioId) {
        Cliente cliente = (Cliente) usuarioService.buscarPorId(usuarioId);
        return consentimentoRepository.findByClienteAndAtivoTrue(cliente).orElse(null);
    }

    @Transactional
    public void revogarConsentimento(Long usuarioId) {
        Cliente cliente = (Cliente) usuarioService.buscarPorId(usuarioId);
        consentimentoRepository.findByClienteAndAtivoTrue(cliente)
                .ifPresent(cons -> cons.setAtivo(false));
    }
}