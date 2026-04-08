/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.entity.Unidade;
import com.raizes.raizes_backend.domain.entity.Usuario;
import com.raizes.raizes_backend.domain.repository.UnidadeRepository;
import com.raizes.raizes_backend.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UnidadeService {

    @Autowired
    private UnidadeRepository unidadeRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Page<Unidade> listarUnidades(Pageable pageable) {
        return unidadeRepository.findAll(pageable);
    }

    public Unidade buscarPorId(Long id) {
        return unidadeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Unidade não encontrada"));
    }

    @Transactional
    public Unidade criarUnidade(Unidade unidade, Long gerenteId) {
        Usuario gerente = usuarioRepository.findById(gerenteId)
                .orElseThrow(() -> new RuntimeException("Gerente não encontrado"));
        unidade.setGerente(gerente);
        unidade.setAtiva(true);
        return unidadeRepository.save(unidade);
    }

    @Transactional
    public Unidade atualizarUnidade(Long id, Unidade unidadeAtualizada, Long gerenteId) {
        Unidade unidade = buscarPorId(id);
        unidade.setNome(unidadeAtualizada.getNome());
        unidade.setEndereco(unidadeAtualizada.getEndereco());
        unidade.setHorarioFuncionamento(unidadeAtualizada.getHorarioFuncionamento());
        if (gerenteId != null) {
            Usuario gerente = usuarioRepository.findById(gerenteId)
                    .orElseThrow(() -> new RuntimeException("Gerente não encontrado"));
            unidade.setGerente(gerente);
        }
        return unidadeRepository.save(unidade);
    }

    @Transactional
    public void desativarUnidade(Long id) {
        Unidade unidade = buscarPorId(id);
        unidade.setAtiva(false);
        unidadeRepository.save(unidade);
    }
}