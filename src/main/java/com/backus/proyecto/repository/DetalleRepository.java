package com.backus.proyecto.repository;

import com.backus.proyecto.entity.Detalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DetalleRepository extends JpaRepository<Detalle, Long> {
    @Query("SELECT d.producto, SUM(d.cantidad) FROM Detalle d GROUP BY d.producto ORDER BY SUM(d.cantidad) DESC")
    List<Object[]> findMostSoldProducts();
    List<Detalle> findByPedido_IdPedido(Long id); // Cambiado a Long para consistencia
}
