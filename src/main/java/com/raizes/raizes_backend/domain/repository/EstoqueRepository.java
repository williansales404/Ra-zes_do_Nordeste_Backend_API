/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.raizes.raizes_backend.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.raizes.raizes_backend.domain.entity.Estoque;

public interface EstoqueRepository extends JpaRepository<Estoque, Long> {
    
}
