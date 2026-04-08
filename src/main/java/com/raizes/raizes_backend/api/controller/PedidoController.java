/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.*;
import com.raizes.raizes_backend.application.service.PedidoService;
import com.raizes.raizes_backend.application.service.UsuarioService;
import com.raizes.raizes_backend.domain.entity.Cliente;
import com.raizes.raizes_backend.domain.entity.Pedido;
import com.raizes.raizes_backend.domain.entity.Usuario;
import com.raizes.raizes_backend.domain.enums.CanalPedido;
import com.raizes.raizes_backend.domain.enums.StatusPedido;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody PedidoRequestDTO request,
                                                         @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        if (!(usuario instanceof Cliente)) {
            throw new RuntimeException("Apenas clientes podem criar pedidos diretamente");
        }
        Pedido pedido = pedidoService.criarPedido(request, (Cliente) usuario);
        PedidoResponseDTO response = toResponseDTO(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponseDTO>> listarPedidos(
            @RequestParam(required = false) Long unidadeId,
            @RequestParam(required = false) StatusPedido status,
            @RequestParam(required = false) CanalPedido canalPedido,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Long clienteId = (usuario instanceof Cliente) ? usuario.getId() : null;
        // Se for gerente, pode ver todos; se for cliente, só seus pedidos
        Page<Pedido> pedidos = pedidoService.listarPedidos(
                (usuario.getTipo().name().equals("GERENTE") ? null : clienteId),
                unidadeId, status, canalPedido, PageRequest.of(page, size));
        Page<PedidoResponseDTO> response = pedidos.map(this::toResponseDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(@PathVariable Long id,
                                                          @AuthenticationPrincipal UserDetails userDetails) {
        Pedido pedido = pedidoService.buscarPorId(id);
        // Verificar permissão: cliente só vê seus pedidos, outros podem ver (simplificado)
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        if (usuario instanceof Cliente && !pedido.getCliente().getId().equals(usuario.getId())) {
            throw new RuntimeException("Acesso negado");
        }
        return ResponseEntity.ok(toResponseDTO(pedido));
    }
    
    @GetMapping("/{id}/pagamento")
    public ResponseEntity<PagamentoResponseDTO> consultarPagamento(@PathVariable Long id) {
        Pedido pedido = pedidoService.buscarPorId(id);
        if (pedido.getPagamento() == null) {
            return ResponseEntity.notFound().build();
        }
        PagamentoResponseDTO response = new PagamentoResponseDTO(
                pedido.getPagamento().getStatus(),
                pedido.getPagamento().getMetodo()
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestBody StatusUpdateRequestDTO request,
                                                             @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Pedido pedido = pedidoService.atualizarStatus(id, request.getStatus(), usuario.getTipo().name());
        return ResponseEntity.ok(toResponseDTO(pedido));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelarPedido(@PathVariable Long id,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        pedidoService.cancelarPedido(id, usuario.getTipo().name());
        return ResponseEntity.noContent().build();
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getDataCriacao(),
                pedido.getCanal().name(),
                pedido.getItens().stream()
                        .map(i -> new ItemPedidoResponseDTO(
                                i.getProduto().getId(),
                                i.getProduto().getNome(),
                                i.getQuantidade(),
                                i.getPrecoUnitario()))
                        .collect(Collectors.toList())
        );
    }
}
