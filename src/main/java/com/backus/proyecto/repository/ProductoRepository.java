package com.backus.proyecto.repository;


import com.backus.proyecto.entity.Categoria;
import com.backus.proyecto.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    Categoria findByidProducto(Integer idProducto);
}
