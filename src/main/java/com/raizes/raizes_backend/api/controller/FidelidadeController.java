/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.PontosResponseDTO;
import com.raizes.raizes_backend.api.dto.ResgatePontosRequestDTO;
import com.raizes.raizes_backend.application.service.FidelidadeService;
import com.raizes.raizes_backend.application.service.UsuarioService;
import com.raizes.raizes_backend.domain.entity.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fidelidade")
public class FidelidadeController {

    @Autowired
    private FidelidadeService fidelidadeService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<PontosResponseDTO> consultarPontos(@AuthenticationPrincipal UserDetails userDetails) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Integer pontos = fidelidadeService.consultarPontos(usuario.getId());
        return ResponseEntity.ok(new PontosResponseDTO(pontos));
    }

    @PostMapping("/resgatar")
    public ResponseEntity<Map<String, Double>> resgatarPontos(@AuthenticationPrincipal UserDetails userDetails,
                                                               @RequestBody ResgatePontosRequestDTO request) {
        Usuario usuario = usuarioService.buscarPorEmail(userDetails.getUsername());
        Double desconto = fidelidadeService.resgatarPontos(usuario.getId(), request.getPontos());
        Map<String, Double> response = new HashMap<>();
        response.put("descontoAplicado", desconto);
        return ResponseEntity.ok(response);
    }
}