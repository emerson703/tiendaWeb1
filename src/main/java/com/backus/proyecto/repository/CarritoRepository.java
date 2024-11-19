package com.backus.proyecto.repository;

import com.backus.proyecto.entity.Carrito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CarritoRepository extends JpaRepository <Carrito, Integer> {
    // Encuentra un carrito activo de un cliente en específico


}
