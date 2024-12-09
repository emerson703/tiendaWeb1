package com.backus.proyecto.repository;


import com.backus.proyecto.entity.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query("SELECT p.cliente, COUNT(p) FROM Pedido p GROUP BY p.cliente ORDER BY COUNT(p) DESC")
    List<Object[]> findMostFrequentClients();
List<Pedido> findByEstado(String estado);
List<Pedido> findByClienteIdCliente(Integer id);
List<Pedido> findByRepartidorIdAndEstado(Long repartidorId, String estado);

}
