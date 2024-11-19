package com.backus.proyecto.repository;

import com.backus.proyecto.entity.Repartidor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepartidorRepository extends JpaRepository<Repartidor, Long> {
    Repartidor findByCorreo(String correo);

}
