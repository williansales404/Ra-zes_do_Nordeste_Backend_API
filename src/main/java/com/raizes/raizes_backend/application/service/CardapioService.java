/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.entity.*;
import com.raizes.raizes_backend.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CardapioService {

    @Autowired
    private CardapioRepository cardapioRepository;

    @Autowired
    private ItemCardapioRepository itemCardapioRepository;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private ProdutoService produtoService;

    // Obtém ou cria o cardápio de uma unidade
    private Cardapio obterOuCriarCardapio(Long unidadeId) {
        Unidade unidade = unidadeService.buscarPorId(unidadeId);
        return cardapioRepository.findByUnidade(unidade)
                .orElseGet(() -> {
                    Cardapio novo = new Cardapio(unidade);
                    return cardapioRepository.save(novo);
                });
    }

    public List<ItemCardapio> listarCardapioPorUnidade(Long unidadeId) {
        Cardapio cardapio = obterOuCriarCardapio(unidadeId);
        return cardapio.getItens();
    }

    @Transactional
    public ItemCardapio adicionarItemAoCardapio(Long unidadeId, Long produtoId, Double precoPersonalizado) {
        Cardapio cardapio = obterOuCriarCardapio(unidadeId);
        Produto produto = produtoService.buscarPorId(produtoId);

        // Verifica se o item já existe
        boolean existe = cardapio.getItens().stream()
                .anyMatch(item -> item.getProduto().getId().equals(produtoId));
        if (existe) {
            throw new RuntimeException("Produto já está no cardápio desta unidade");
        }

        ItemCardapio novoItem = new ItemCardapio(produto, cardapio, precoPersonalizado);
        cardapio.getItens().add(novoItem);
        return itemCardapioRepository.save(novoItem);
    }

    @Transactional
    public ItemCardapio atualizarItemCardapio(Long itemId, Double precoPersonalizado, Boolean disponivel) {
        ItemCardapio item = itemCardapioRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item do cardápio não encontrado"));
        if (precoPersonalizado != null) item.setPrecoPersonalizado(precoPersonalizado);
        if (disponivel != null) item.setDisponivel(disponivel);
        return itemCardapioRepository.save(item);
    }

    @Transactional
    public void removerItemDoCardapio(Long itemId) {
        itemCardapioRepository.deleteById(itemId);
    }
}