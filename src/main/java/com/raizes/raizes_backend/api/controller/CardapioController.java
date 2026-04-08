/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.ItemCardapioRequestDTO;
import com.raizes.raizes_backend.api.dto.ItemCardapioResponseDTO;
import com.raizes.raizes_backend.application.service.CardapioService;
import com.raizes.raizes_backend.domain.entity.ItemCardapio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cardapio")
public class CardapioController {

    @Autowired
    private CardapioService cardapioService;

    // Consultar cardápio por unidade (público)
    @GetMapping
    public ResponseEntity<List<ItemCardapioResponseDTO>> consultarCardapio(
            @RequestParam("unidadeId") Long unidadeId) {
        List<ItemCardapio> itens = cardapioService.listarCardapioPorUnidade(unidadeId);
        List<ItemCardapioResponseDTO> response = itens.stream()
                .map(item -> new ItemCardapioResponseDTO(
                        item.getId(),
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getPrecoPersonalizado() != null ? item.getPrecoPersonalizado() : item.getProduto().getPrecoBase(),
                        item.getDisponivel()
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // Adicionar produto ao cardápio de uma unidade (apenas gerente)
    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ItemCardapioResponseDTO> adicionarItem(
            @RequestParam("unidadeId") Long unidadeId,
            @RequestBody ItemCardapioRequestDTO dto) {
        ItemCardapio item = cardapioService.adicionarItemAoCardapio(
                unidadeId, dto.getProdutoId(), dto.getPrecoPersonalizado());
        ItemCardapioResponseDTO response = new ItemCardapioResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getPrecoPersonalizado() != null ? item.getPrecoPersonalizado() : item.getProduto().getPrecoBase(),
                item.getDisponivel()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // Atualizar preço/disponibilidade de um item do cardápio
    @PutMapping("/{itemId}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ItemCardapioResponseDTO> atualizarItem(
            @PathVariable Long itemId,
            @RequestBody ItemCardapioRequestDTO dto) {
        ItemCardapio item = cardapioService.atualizarItemCardapio(itemId, dto.getPrecoPersonalizado(), dto.getDisponivel());
        ItemCardapioResponseDTO response = new ItemCardapioResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getPrecoPersonalizado() != null ? item.getPrecoPersonalizado() : item.getProduto().getPrecoBase(),
                item.getDisponivel()
        );
        return ResponseEntity.ok(response);
    }

    // Remover item do cardápio
    @DeleteMapping("/{itemId}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<Void> removerItem(@PathVariable Long itemId) {
        cardapioService.removerItemDoCardapio(itemId);
        return ResponseEntity.noContent().build();
    }
}