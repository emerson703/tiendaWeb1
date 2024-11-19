package com.backus.proyecto.repository;

import com.backus.proyecto.entity.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DetalleRepository extends JpaRepository<Detalle, Long> {
    List<Detalle> findByPedido_IdPedido(Long id); // Cambiado a Long para consistencia
}
