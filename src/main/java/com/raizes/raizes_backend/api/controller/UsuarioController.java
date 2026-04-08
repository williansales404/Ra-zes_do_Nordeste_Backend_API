/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.UsuarioResponseDTO;
import com.raizes.raizes_backend.api.dto.UsuarioUpdateDTO;
import com.raizes.raizes_backend.application.service.UsuarioService;
import com.raizes.raizes_backend.domain.entity.Cliente;
import com.raizes.raizes_backend.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> obterPerfil(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Integer pontos = (usuario instanceof Cliente) ? ((Cliente) usuario).getPontos() : null;
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone(),
                usuario.getTipo().name(),
                pontos
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping("/me")
    public ResponseEntity<UsuarioResponseDTO> atualizarPerfil(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestBody UsuarioUpdateDTO dto) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Usuario atualizado = usuarioService.atualizarDados(usuario.getId(), dto.getNome(), dto.getTelefone());
        Integer pontos = (atualizado instanceof Cliente) ? ((Cliente) atualizado).getPontos() : null;
        UsuarioResponseDTO response = new UsuarioResponseDTO(
                atualizado.getId(),
                atualizado.getNome(),
                atualizado.getEmail(),
                atualizado.getTelefone(),
                atualizado.getTipo().name(),
                pontos
        );
        return ResponseEntity.ok(response);
    }
}