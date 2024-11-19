package com.backus.proyecto.repository;



import com.backus.proyecto.entity.Categoria;
import com.backus.proyecto.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntregaRepository extends JpaRepository<Entrega, Integer> {

}
