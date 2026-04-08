/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.entity.Usuario;
import com.raizes.raizes_backend.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    @Transactional
    public Usuario atualizarDados(Long id, String nome, String telefone) {
        Usuario usuario = buscarPorId(id);
        if (nome != null && !nome.isBlank()) usuario.setNome(nome);
        if (telefone != null && !telefone.isBlank()) usuario.setTelefone(telefone);
        return usuarioRepository.save(usuario);
    }
}
