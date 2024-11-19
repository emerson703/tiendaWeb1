package com.backus.proyecto.repository;


import com.backus.proyecto.entity.Pedido;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
List<Pedido> findByEstado(String estado);
List<Pedido> findByClienteIdCliente(Integer id);
List<Pedido> findByRepartidorIdAndEstado(Long repartidorId, String estado);

}
