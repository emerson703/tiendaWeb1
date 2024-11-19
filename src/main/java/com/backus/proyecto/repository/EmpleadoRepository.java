package com.backus.proyecto.repository;


import com.backus.proyecto.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Empleado findByCorreo(String correo);
}