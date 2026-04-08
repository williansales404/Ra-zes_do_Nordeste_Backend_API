/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.application.service;

import com.raizes.raizes_backend.api.dto.ItemPedidoRequestDTO;
import com.raizes.raizes_backend.api.dto.PedidoRequestDTO;
import com.raizes.raizes_backend.domain.entity.*;
import com.raizes.raizes_backend.domain.enums.CanalPedido;
import com.raizes.raizes_backend.domain.enums.StatusPagamento;
import com.raizes.raizes_backend.domain.enums.StatusPedido;
import com.raizes.raizes_backend.domain.exception.EstoqueInsuficienteException;
import com.raizes.raizes_backend.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class PedidoService {

    private static final Logger log = LoggerFactory.getLogger(PedidoService.class);
    
    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UnidadeService unidadeService;

    @Autowired
    private ProdutoService produtoService;

    @Autowired
    private EstoqueService estoqueService;

    @Autowired
    private PagamentoMockService pagamentoMockService;

    @Autowired
    private FidelidadeService fidelidadeService;

    @Autowired
    private ConsentimentoService consentimentoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    // Criar pedido (fluxo principal)
    @Transactional
    public Pedido criarPedido(PedidoRequestDTO request, Cliente cliente) {
        // 1. Validar unidade
        Unidade unidade = unidadeService.buscarPorId(request.getUnidadeId());
        if (!unidade.isAtiva()) {
            throw new RuntimeException("Unidade inativa");
        }

        // 2. Validar itens e montar mapa (produtoId -> quantidade)
        Map<Long, Integer> produtosQuantidades = new HashMap<>();
        List<ItemPedidoRequestDTO> itensDTO = request.getItens();
        if (itensDTO == null || itensDTO.isEmpty()) {
            throw new RuntimeException("Pedido deve conter pelo menos um item");
        }

        // Mapa para armazenar preço unitário de cada produto (buscar do cardápio ou preço base)
        Map<Long, Double> precos = new HashMap<>();
        for (ItemPedidoRequestDTO item : itensDTO) {
            Produto produto = produtoService.buscarPorId(item.getProdutoId());
            produtosQuantidades.put(produto.getId(), item.getQuantidade());
            precos.put(produto.getId(), produto.getPrecoBase());
        }

        // 3. Verificar estoque
        try {
            estoqueService.verificarDisponibilidade(unidade.getId(), produtosQuantidades);
        } catch (EstoqueInsuficienteException e) {
            throw new RuntimeException("Estoque insuficiente: " + e.getMessage());
        }

        // 4. Calcular valor total (aplicar promoções simples: 10% desconto se total > 50)
        double valorTotal = 0.0;
        for (ItemPedidoRequestDTO item : itensDTO) {
            Double preco = precos.get(item.getProdutoId());
            valorTotal += preco * item.getQuantidade();
        }
        if (valorTotal > 50.0) {
            valorTotal = valorTotal * 0.9; // 10% desconto
        }

        // 5. Criar objeto Pedido (status inicial AGUARDANDO_PAGAMENTO)
        Pedido pedido = new Pedido();
        pedido.setCanal(request.getCanalPedido());
        pedido.setCliente(cliente);
        pedido.setUnidade(unidade);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setStatus(StatusPedido.AGUARDANDO_PAGAMENTO);
        pedido.setValorTotal(valorTotal);

        // 6. Processar pagamento (mock)
        StatusPagamento statusPagamento = pagamentoMockService.processar(valorTotal, request.getFormaPagamento());
        Pagamento pagamento = new Pagamento();
        pagamento.setMetodo(request.getFormaPagamento());
        pagamento.setData(LocalDateTime.now());
        pagamento.setTransacaoId(pagamentoMockService.gerarTransacaoId());
        pagamento.setStatus(statusPagamento);
        pagamento.setPedido(pedido);
        pedido.setPagamento(pagamento);

        // 7. Se pagamento APROVADO
        if (statusPagamento == StatusPagamento.APROVADO) {
            // Reservar estoque (já validado)
            estoqueService.reservarEstoque(unidade.getId(), produtosQuantidades);
            // Atualizar status do pedido
            pedido.setStatus(StatusPedido.PAGO);
            // Acumular pontos (se cliente tiver consentimento ativo)
            Consentimento consentimento = consentimentoService.obterConsentimentoAtivo(cliente.getId());
            if (consentimento != null && consentimento.isAtivo()) {
                fidelidadeService.acumularPontos(cliente.getId(), (int) valorTotal);
            }
        } else {
            // Pagamento recusado: status CANCELADO, sem reserva de estoque
            pedido.setStatus(StatusPedido.CANCELADO);
            // Não reserva estoque
        }

        // 8. Salvar itens do pedido
        pedido = pedidoRepository.save(pedido);
        for (ItemPedidoRequestDTO itemDTO : itensDTO) {
            Produto produto = produtoService.buscarPorId(itemDTO.getProdutoId());
            ItemPedido item = new ItemPedido();
            item.setQuantidade(itemDTO.getQuantidade());
            item.setPrecoUnitario(precos.get(produto.getId()));
            item.setProduto(produto);
            item.setPedido(pedido);
            pedido.getItens().add(item);
        }
        // Salvar pagamento (já associado)
        pagamentoRepository.save(pagamento);

        log.info("Pedido criado: id={}, cliente={}, canal={}, valorTotal={}",
             pedido.getId(), cliente.getEmail(), pedido.getCanal(), pedido.getValorTotal());
        
        return pedidoRepository.save(pedido);
    }

    // Listar pedidos com filtros (status, canal, cliente, unidade)
    public Page<Pedido> listarPedidos(Long clienteId, Long unidadeId, StatusPedido status, CanalPedido canal, Pageable pageable) {
        Specification<Pedido> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (clienteId != null) {
                predicates.add(cb.equal(root.get("cliente").get("id"), clienteId));
            }
            if (unidadeId != null) {
                predicates.add(cb.equal(root.get("unidade").get("id"), unidadeId));
            }
            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }
            if (canal != null) {
                predicates.add(cb.equal(root.get("canal"), canal));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        return pedidoRepository.findAll(spec, pageable);
    }

    public Pedido buscarPorId(Long id) {
        return pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    // Atualizar status (com validação de transição e permissão)
    @Transactional
    public Pedido atualizarStatus(Long pedidoId, StatusPedido novoStatus, String papelUsuario) {
        Pedido pedido = buscarPorId(pedidoId);
        StatusPedido atual = pedido.getStatus();

        // Validações de transição
        switch (novoStatus) {
            case EM_PREPARO:
                if (atual != StatusPedido.PAGO) {
                    throw new RuntimeException("Só é possível iniciar preparo de pedidos pagos");
                }
                if (!"COZINHA".equals(papelUsuario)) {
                    throw new RuntimeException("Apenas a cozinha pode alterar para EM_PREPARO");
                }
                break;
            case PRONTO:
                if (atual != StatusPedido.EM_PREPARO) {
                    throw new RuntimeException("Pedido deve estar em preparo para ficar pronto");
                }
                if (!"COZINHA".equals(papelUsuario)) {
                    throw new RuntimeException("Apenas a cozinha pode alterar para PRONTO");
                }
                break;
            case ENTREGUE:
                if (atual != StatusPedido.PRONTO) {
                    throw new RuntimeException("Pedido deve estar pronto para ser entregue");
                }
                if (!"ATENDENTE".equals(papelUsuario)) {
                    throw new RuntimeException("Apenas atendente pode marcar como entregue");
                }
                break;
            case CANCELADO:
                if (atual != StatusPedido.AGUARDANDO_PAGAMENTO && atual != StatusPedido.EM_PREPARO) {
                    throw new RuntimeException("Pedido só pode ser cancelado antes do pagamento ou durante preparo");
                }
                // Se cancelado e já havia reserva de estoque, devolver
                if (atual == StatusPedido.EM_PREPARO || atual == StatusPedido.PAGO) {
                    Map<Long, Integer> itensMap = pedido.getItens().stream()
                            .collect(Collectors.toMap(i -> i.getProduto().getId(), ItemPedido::getQuantidade));
                    estoqueService.reverterEstoque(pedido.getUnidade().getId(), itensMap);
                }
                break;
            default:
                throw new RuntimeException("Transição de status não suportada");
        }

        StatusPedido statusAntigo = atual;
        
        pedido.setStatus(novoStatus);
        log.info("Status do pedido {} alterado de {} para {} por {}",
             pedidoId, statusAntigo, novoStatus, papelUsuario);
        
        return pedidoRepository.save(pedido);
    }

    // Cancelar pedido
    @Transactional
    public void cancelarPedido(Long pedidoId, String papelUsuario) {
        Pedido pedido = buscarPorId(pedidoId);
        if (papelUsuario.equals("CLIENTE") && !pedido.getCliente().getId().equals(getCurrentUserId())) {
            throw new RuntimeException("Você só pode cancelar seus próprios pedidos");
        }
        atualizarStatus(pedidoId, StatusPedido.CANCELADO, papelUsuario);
    }

    // Método auxiliar para obter ID do usuário atual 
    private Long getCurrentUserId() {
        return 0L;
    }
}