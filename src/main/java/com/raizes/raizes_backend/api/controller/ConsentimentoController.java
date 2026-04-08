/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.ConsentimentoRequestDTO;
import com.raizes.raizes_backend.api.dto.ConsentimentoResponseDTO;
import com.raizes.raizes_backend.application.service.ConsentimentoService;
import com.raizes.raizes_backend.application.service.UsuarioService;
import com.raizes.raizes_backend.domain.entity.Consentimento;
import com.raizes.raizes_backend.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consentimento")
public class ConsentimentoController {

    @Autowired
    private ConsentimentoService consentimentoService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<ConsentimentoResponseDTO> registrarConsentimento(@AuthenticationPrincipal UserDetails userDetails,
                                                                            @RequestBody ConsentimentoRequestDTO dto) {
        if (!dto.isAceito()) {
            throw new RuntimeException("É necessário aceitar o termo para registrar consentimento");
        }
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Consentimento consentimento = consentimentoService.registrarConsentimento(usuario.getId(), dto.getVersaoTermo());
        ConsentimentoResponseDTO response = new ConsentimentoResponseDTO(
                consentimento.getDataRegistro(),
                consentimento.getVersaoTermo(),
                consentimento.isAtivo()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<ConsentimentoResponseDTO> obterConsentimento(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Consentimento consentimento = consentimentoService.obterConsentimentoAtivo(usuario.getId());
        if (consentimento == null) {
            return ResponseEntity.noContent().build();
        }
        ConsentimentoResponseDTO response = new ConsentimentoResponseDTO(
                consentimento.getDataRegistro(),
                consentimento.getVersaoTermo(),
                consentimento.isAtivo()
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<Void> revogarConsentimento(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        consentimentoService.revogarConsentimento(usuario.getId());
        return ResponseEntity.noContent().build();
    }
}