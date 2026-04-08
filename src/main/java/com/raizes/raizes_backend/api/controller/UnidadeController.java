/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.UnidadeRequestDTO;
import com.raizes.raizes_backend.api.dto.UnidadeResponseDTO;
import com.raizes.raizes_backend.application.service.UnidadeService;
import com.raizes.raizes_backend.domain.entity.Unidade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/unidades")
public class UnidadeController {

    @Autowired
    private UnidadeService unidadeService;

    @GetMapping
    public ResponseEntity<Page<UnidadeResponseDTO>> listarUnidades(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Unidade> unidades = unidadeService.listarUnidades(PageRequest.of(page, size));
        Page<UnidadeResponseDTO> response = unidades.map(u ->
                new UnidadeResponseDTO(u.getId(), u.getNome(), u.getEndereco(),
                        u.getHorarioFuncionamento(), u.isAtiva(),
                        u.getGerente() != null ? u.getGerente().getNome() : null));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UnidadeResponseDTO> buscarUnidade(@PathVariable Long id) {
        Unidade u = unidadeService.buscarPorId(id);
        UnidadeResponseDTO response = new UnidadeResponseDTO(u.getId(), u.getNome(), u.getEndereco(),
                u.getHorarioFuncionamento(), u.isAtiva(),
                u.getGerente() != null ? u.getGerente().getNome() : null);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<UnidadeResponseDTO> criarUnidade(@RequestBody UnidadeRequestDTO dto) {
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());
        unidade.setEndereco(dto.getEndereco());
        unidade.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        Unidade salva = unidadeService.criarUnidade(unidade, dto.getGerenteId());
        UnidadeResponseDTO response = new UnidadeResponseDTO(salva.getId(), salva.getNome(), salva.getEndereco(),
                salva.getHorarioFuncionamento(), salva.isAtiva(),
                salva.getGerente().getNome());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<UnidadeResponseDTO> atualizarUnidade(@PathVariable Long id, @RequestBody UnidadeRequestDTO dto) {
        Unidade unidade = new Unidade();
        unidade.setNome(dto.getNome());
        unidade.setEndereco(dto.getEndereco());
        unidade.setHorarioFuncionamento(dto.getHorarioFuncionamento());
        Unidade atualizada = unidadeService.atualizarUnidade(id, unidade, dto.getGerenteId());
        UnidadeResponseDTO response = new UnidadeResponseDTO(atualizada.getId(), atualizada.getNome(), atualizada.getEndereco(),
                atualizada.getHorarioFuncionamento(), atualizada.isAtiva(),
                atualizada.getGerente().getNome());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> desativarUnidade(@PathVariable Long id) {
        unidadeService.desativarUnidade(id);
        return ResponseEntity.noContent().build();
    }
}