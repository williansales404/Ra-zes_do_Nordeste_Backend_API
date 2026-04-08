/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.EstoqueAjusteDTO;
import com.raizes.raizes_backend.api.dto.EstoqueResponseDTO;
import com.raizes.raizes_backend.api.dto.EstoqueUpdateDTO;
import com.raizes.raizes_backend.application.service.EstoqueService;
import com.raizes.raizes_backend.domain.entity.Estoque;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/estoque")
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;

    // Consultar estoque completo de uma unidade (GERENTE, ATENDENTE, COZINHA)
    @GetMapping
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE', 'COZINHA')")
    public ResponseEntity<List<EstoqueResponseDTO>> consultarEstoquePorUnidade(
            @RequestParam("unidadeId") Long unidadeId) {
        List<Estoque> estoques = estoqueService.listarEstoquePorUnidade(unidadeId);
        List<EstoqueResponseDTO> response = estoques.stream()
                .map(e -> new EstoqueResponseDTO(
                        e.getProduto().getId(),
                        e.getProduto().getNome(),
                        e.getQuantidade()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Consultar estoque de um produto específico
    @GetMapping("/{produtoId}")
    @PreAuthorize("hasAnyRole('GERENTE', 'ATENDENTE', 'COZINHA')")
    public ResponseEntity<EstoqueResponseDTO> consultarEstoqueProduto(
            @RequestParam("unidadeId") Long unidadeId,
            @PathVariable Long produtoId) {
        Estoque estoque = estoqueService.buscarEstoque(unidadeId, produtoId);
        EstoqueResponseDTO response = new EstoqueResponseDTO(
                estoque.getProduto().getId(),
                estoque.getProduto().getNome(),
                estoque.getQuantidade());
        return ResponseEntity.ok(response);
    }

    // Atualizar estoque (definir quantidade absoluta) – apenas GERENTE
    @PutMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> atualizarEstoque(@RequestBody EstoqueUpdateDTO dto) {
        estoqueService.atualizarEstoque(dto.getUnidadeId(), dto.getProdutoId(), dto.getQuantidade());
        return ResponseEntity.noContent().build();
    }

    // Ajuste de estoque (entrada/saída) – apenas GERENTE
    @PostMapping("/ajuste")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> ajustarEstoque(@RequestBody EstoqueAjusteDTO dto) {
        estoqueService.ajustarEstoque(dto.getUnidadeId(), dto.getProdutoId(), dto.getQuantidade());
        return ResponseEntity.noContent().build();
    }
}
