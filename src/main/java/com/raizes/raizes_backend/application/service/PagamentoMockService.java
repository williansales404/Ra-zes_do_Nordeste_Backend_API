/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.domain.enums.StatusPagamento;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class PagamentoMockService {

    /**
     * Simula processamento de pagamento.
     * Regras:
     * - Aprovado se o valor total não for zero.
     * - Recusado se o valor total for múltiplo de 100.
     * @param valorTotal valor do pedido
     * @param metodo forma de pagamento (sempre "MOCK")
     * @return StatusPagamento.APROVADO ou RECUSADO
     */
    public StatusPagamento processar(Double valorTotal, String metodo) {
        // Regra didática: aprovado se o valor total não for múltiplo de 100
        if (valorTotal % 100 == 0) {
            return StatusPagamento.RECUSADO;
        }
        return StatusPagamento.APROVADO;
    }

    public String gerarTransacaoId() {
        return "MOCK-" + UUID.randomUUID().toString();
    }
}