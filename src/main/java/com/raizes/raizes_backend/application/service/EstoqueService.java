/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.entity.Estoque;
import com.raizes.raizes_backend.domain.entity.Produto;
import com.raizes.raizes_backend.domain.entity.Unidade;
import com.raizes.raizes_backend.domain.exception.EstoqueInsuficienteException;
import com.raizes.raizes_backend.domain.repository.EstoqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

@Service
public class EstoqueService {

    @Autowired
    private EstoqueRepository estoqueRepository;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private ProdutoService produtoService;

    // Consultar estoque completo de uma unidade
    public List<Estoque> listarEstoquePorUnidade(Long unidadeId) {
        Unidade unidade = unidadeService.buscarPorId(unidadeId);
        return estoqueRepository.findByUnidade(unidade);
    }

    // Consultar estoque de um produto específico em uma unidade
    public Estoque buscarEstoque(Long unidadeId, Long produtoId) {
        Unidade unidade = unidadeService.buscarPorId(unidadeId);
        Produto produto = produtoService.buscarPorId(produtoId);
        return estoqueRepository.findByUnidadeAndProduto(unidade, produto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto na unidade"));
    }

    // Atualizar estoque (definir quantidade absoluta) – usado pelo GERENTE
    @Transactional
    public Estoque atualizarEstoque(Long unidadeId, Long produtoId, Integer novaQuantidade) {
        Estoque estoque = buscarEstoque(unidadeId, produtoId);
        estoque.setQuantidade(novaQuantidade);
        return estoqueRepository.save(estoque);
    }

    // Ajustar estoque (entrada/saída) – usado pelo GERENTE
    @Transactional
    public Estoque ajustarEstoque(Long unidadeId, Long produtoId, Integer ajuste) {
        Estoque estoque = buscarEstoque(unidadeId, produtoId);
        int novaQuantidade = estoque.getQuantidade() + ajuste;
        if (novaQuantidade < 0) {
            throw new RuntimeException("Ajuste resultaria em estoque negativo");
        }
        estoque.setQuantidade(novaQuantidade);
        return estoqueRepository.save(estoque);
    }

    // ========== MÉTODOS PARA VALIDAÇÃO E RESERVA (usados no Commit 7) ==========

    /**
     * Verifica se todos os itens de um pedido estão disponíveis no estoque da unidade.
     * @param unidadeId ID da unidade
     * @param produtosQuantidades Mapa (produtoId -> quantidade solicitada)
     * @throws EstoqueInsuficienteException se algum item não tiver quantidade suficiente
     */
    public void verificarDisponibilidade(Long unidadeId, Map<Long, Integer> produtosQuantidades) {
        Unidade unidade = unidadeService.buscarPorId(unidadeId);
        for (Map.Entry<Long, Integer> entry : produtosQuantidades.entrySet()) {
            Long produtoId = entry.getKey();
            Integer quantidadeSolicitada = entry.getValue();
            Produto produto = produtoService.buscarPorId(produtoId);
            Estoque estoque = estoqueRepository.findByUnidadeAndProduto(unidade, produto)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque da unidade"));
            if (estoque.getQuantidade() < quantidadeSolicitada) {
                throw new EstoqueInsuficienteException(
                        String.format("Estoque insuficiente para o produto %s. Disponível: %d, Solicitado: %d",
                                produto.getNome(), estoque.getQuantidade(), quantidadeSolicitada));
            }
        }
    }

    /**
     * Reserva (diminui) o estoque para os itens de um pedido. Deve ser chamado APÓS a verificação.
     * @param unidadeId ID da unidade
     * @param produtosQuantidades Mapa (produtoId -> quantidade a reservar)
     */
    @Transactional
    public void reservarEstoque(Long unidadeId, Map<Long, Integer> produtosQuantidades) {
        Unidade unidade = unidadeService.buscarPorId(unidadeId);
        for (Map.Entry<Long, Integer> entry : produtosQuantidades.entrySet()) {
            Long produtoId = entry.getKey();
            Integer quantidade = entry.getValue();
            Produto produto = produtoService.buscarPorId(produtoId);
            Estoque estoque = estoqueRepository.findByUnidadeAndProduto(unidade, produto)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque da unidade"));
            estoque.setQuantidade(estoque.getQuantidade() - quantidade);
            estoqueRepository.save(estoque);
        }
    }

    /**
     * Reverte o estoque (devolve) em caso de cancelamento de pedido ou falha no pagamento.
     * @param unidadeId ID da unidade
     * @param produtosQuantidades Mapa (produtoId -> quantidade a devolver)
     */
    @Transactional
    public void reverterEstoque(Long unidadeId, Map<Long, Integer> produtosQuantidades) {
        Unidade unidade = unidadeService.buscarPorId(unidadeId);
        for (Map.Entry<Long, Integer> entry : produtosQuantidades.entrySet()) {
            Long produtoId = entry.getKey();
            Integer quantidade = entry.getValue();
            Produto produto = produtoService.buscarPorId(produtoId);
            Estoque estoque = estoqueRepository.findByUnidadeAndProduto(unidade, produto)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado no estoque da unidade"));
            estoque.setQuantidade(estoque.getQuantidade() + quantidade);
            estoqueRepository.save(estoque);
        }
    }
}