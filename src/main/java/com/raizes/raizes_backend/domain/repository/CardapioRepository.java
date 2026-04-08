/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.raizes.raizes_backend.domain.repository;

import com.raizes.raizes_backend.domain.entity.Cardapio;
import com.raizes.raizes_backend.domain.entity.Unidade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CardapioRepository extends JpaRepository<Cardapio, Long> {
    Optional<Cardapio> findByUnidade(Unidade unidade);
}