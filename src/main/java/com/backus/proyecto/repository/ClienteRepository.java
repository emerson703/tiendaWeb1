package com.backus.proyecto.repository;


import com.backus.proyecto.entity.Cliente;
import com.backus.proyecto.entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Cliente findByEmail(String email);
    Cliente findByEmailAndClave(String email, String clave);
}