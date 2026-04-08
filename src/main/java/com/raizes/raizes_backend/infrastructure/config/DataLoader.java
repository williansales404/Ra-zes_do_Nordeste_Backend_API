/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.raizes.raizes_backend.infrastructure.config;

import com.raizes.raizes_backend.domain.entity.*;
import com.raizes.raizes_backend.domain.enums.TipoUsuario;
import com.raizes.raizes_backend.domain.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner{
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private UnidadeRepository unidadeRepository;
    @Autowired private ProdutoRepository produtoRepository;
    @Autowired private EstoqueRepository estoqueRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    
     @Override
    public void run(String... args) throws Exception {
        // Criar gerente admin se não existir
        if (usuarioRepository.findByEmail("gerente@raizes.com").isEmpty()) {
            Gerente gerente = new Gerente();
            gerente.setNome("Gerente Geral");
            gerente.setEmail("gerente@raizes.com");
            gerente.setSenha(passwordEncoder.encode("admin123"));
            gerente.setTelefone("81999999999");
            gerente.setTipo(TipoUsuario.GERENTE);
            gerente.setDataCadastro(LocalDateTime.now());
            gerente.setAtivo(true);
            usuarioRepository.save(gerente);
        }

        // Criar unidade exemplo
        Unidade unidade = null;
        if (unidadeRepository.count() == 0) {
            unidade = new Unidade();
            unidade.setNome("Unidade Centro - Recife");
            unidade.setEndereco("Rua da Aurora, 123, Recife/PE");
            unidade.setHorarioFuncionamento("08:00 às 22:00");
            unidade.setAtiva(true);
            // buscar gerente existente
            Usuario gerente = usuarioRepository.findByEmail("gerente@raizes.com").orElse(null);
            unidade.setGerente(gerente);
            unidade = unidadeRepository.save(unidade);
        } else {
            unidade = unidadeRepository.findAll().get(0);
        }

        //Criação de Cliente para testes
        if (usuarioRepository.findByEmail("cliente@email.com").isEmpty()) {
            Cliente cliente = new Cliente();
            cliente.setNome("Cliente Teste");
            cliente.setEmail("cliente@email.com");
            cliente.setSenha(passwordEncoder.encode("123456"));
            cliente.setTelefone("81988887777");
            cliente.setTipo(TipoUsuario.CLIENTE);
            cliente.setDataCadastro(LocalDateTime.now());
            cliente.setAtivo(true);
            cliente.setPontos(100); // pontos iniciais para teste
            usuarioRepository.save(cliente);
        }

        // Criar produtos
        if (produtoRepository.count() == 0) {
            Produto p1 = new Produto();
            p1.setNome("Baião de Dois");
            p1.setDescricao("Arroz, feijão verde, queijo coalho, carne de sol");
            p1.setPrecoBase(39.90);
            produtoRepository.save(p1);

            Produto p2 = new Produto();
            p2.setNome("Tapioca de Carne de Sol");
            p2.setDescricao("Tapioca recheada com carne de sol e queijo");
            p2.setPrecoBase(24.90);
            produtoRepository.save(p2);

            Produto p3 = new Produto();
            p3.setNome("Cuscuz com Ovo");
            p3.setDescricao("Cuscuz nordestino com ovo frito e manteiga");
            p3.setPrecoBase(18.90);
            produtoRepository.save(p3);
        }

        // Criar estoque inicial para a unidade
        if (estoqueRepository.count() == 0 && unidade != null) {
            for (Produto p : produtoRepository.findAll()) {
                Estoque e = new Estoque();
                e.setUnidade(unidade);
                e.setProduto(p);
                e.setQuantidade(50); // estoque inicial
                estoqueRepository.save(e);
            }
        }
        
        // Criar cozinha
        if (usuarioRepository.findByEmail("cozinha@raizes.com").isEmpty()) {
            Cozinha cozinha = new Cozinha();
            cozinha.setNome("Cozinha Padrão");
            cozinha.setEmail("cozinha@raizes.com");
            cozinha.setSenha(passwordEncoder.encode("123456"));
            cozinha.setTelefone("81999998888");
            cozinha.setTipo(TipoUsuario.COZINHA);
            cozinha.setDataCadastro(LocalDateTime.now());
            cozinha.setAtivo(true);
            usuarioRepository.save(cozinha);
        }
        // Criar atendente
        if (usuarioRepository.findByEmail("atendente@raizes.com").isEmpty()) {
            Atendente atendente = new Atendente();
            atendente.setNome("Atendente Padrão");
            atendente.setEmail("atendente@raizes.com");
            atendente.setSenha(passwordEncoder.encode("123456"));
            atendente.setTelefone("81999997777");
            atendente.setTipo(TipoUsuario.ATENDENTE);
            atendente.setDataCadastro(LocalDateTime.now());
            atendente.setAtivo(true);
            usuarioRepository.save(atendente);
        }
    }
}
