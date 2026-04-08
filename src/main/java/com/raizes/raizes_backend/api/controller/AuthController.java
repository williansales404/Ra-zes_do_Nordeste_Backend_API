/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.api.controller;

import com.raizes.raizes_backend.api.dto.LoginRequest;
import com.raizes.raizes_backend.api.dto.LoginResponse;
import com.raizes.raizes_backend.api.dto.RegisterRequest;
import com.raizes.raizes_backend.domain.entity.Cliente;
import com.raizes.raizes_backend.domain.entity.Usuario;
import com.raizes.raizes_backend.domain.enums.TipoUsuario;
import com.raizes.raizes_backend.domain.repository.UsuarioRepository;
import com.raizes.raizes_backend.infrastructure.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Long jwtExpiration;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
            );
        } catch (BadCredentialsException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "CREDENCIAIS_INVALIDAS");
            error.put("message", "E-mail ou senha inválidos.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail()).orElseThrow();
        LoginResponse response = new LoginResponse(
                jwt,
                jwtExpiration,
                usuario.getId(),
                usuario.getNome(),
                usuario.getTipo().name()
        );
        
        log.info("Login bem-sucedido! usuarioId={}, nome={} '{}'",
                usuario.getId(), usuario.getNome(), usuario.getTipo().name());
        
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "EMAIL_JA_CADASTRADO");
            error.put("message", "Já existe um usuário com este e-mail.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }

        Cliente novoCliente = new Cliente();
        novoCliente.setNome(request.getNome());
        novoCliente.setEmail(request.getEmail());
        novoCliente.setSenha(passwordEncoder.encode(request.getSenha()));
        novoCliente.setTelefone(request.getTelefone());
        novoCliente.setTipo(TipoUsuario.CLIENTE);
        novoCliente.setDataCadastro(LocalDateTime.now());
        novoCliente.setAtivo(true);

        usuarioRepository.save(novoCliente);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Usuário registrado com sucesso!");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}