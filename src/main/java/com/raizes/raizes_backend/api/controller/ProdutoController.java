/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.ProdutoRequestDTO;
import com.raizes.raizes_backend.api.dto.ProdutoResponseDTO;
import com.raizes.raizes_backend.application.service.ProdutoService;
import com.raizes.raizes_backend.domain.entity.Produto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listarProdutos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Produto> produtos = produtoService.listarProdutos(PageRequest.of(page, size));
        Page<ProdutoResponseDTO> response = produtos.map(p ->
                new ProdutoResponseDTO(p.getId(), p.getNome(), p.getDescricao(), p.getPrecoBase()));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarProduto(@PathVariable Long id) {
        Produto p = produtoService.buscarPorId(id);
        ProdutoResponseDTO response = new ProdutoResponseDTO(p.getId(), p.getNome(), p.getDescricao(), p.getPrecoBase());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ProdutoResponseDTO> criarProduto(@RequestBody ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPrecoBase(dto.getPrecoBase());
        Produto salvo = produtoService.criarProduto(produto);
        ProdutoResponseDTO response = new ProdutoResponseDTO(salvo.getId(), salvo.getNome(), salvo.getDescricao(), salvo.getPrecoBase());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('GERENTE')")
    public ResponseEntity<ProdutoResponseDTO> atualizarProduto(@PathVariable Long id, @RequestBody ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.getNome());
        produto.setDescricao(dto.getDescricao());
        produto.setPrecoBase(dto.getPrecoBase());
        Produto atualizado = produtoService.atualizarProduto(id, produto);
        ProdutoResponseDTO response = new ProdutoResponseDTO(atualizado.getId(), atualizado.getNome(), atualizado.getDescricao(), atualizado.getPrecoBase());
        return ResponseEntity.ok(response);
    }
}