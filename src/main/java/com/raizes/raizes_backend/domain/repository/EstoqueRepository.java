/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.raizes.raizes_backend.domain.repository;

import com.raizes.raizes_backend.domain.entity.Unidade;
import com.raizes.raizes_backend.domain.entity.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import com.raizes.raizes_backend.domain.entity.Estoque;
import java.util.List;
import java.util.Optional;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    Optional<Estoque> findByUnidadeAndProduto(Unidade unidade, Produto produto);
    List<Estoque> findByUnidade(Unidade unidade);
}
