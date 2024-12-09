package com.backus.proyecto.repository;



import com.backus.proyecto.entity.Categoria;
import com.backus.proyecto.entity.Entrega;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Integer> {
    @Query("SELECT em.nombre AS nombreRepartidor, " +
            "em.apellido AS apellidos, " +
            "COUNT(en.idEntrega) AS totalEntregas, " +
            "MAX(en.fechaEntrega) AS ultimaEntrega " +
            "FROM Entrega en " +
            "JOIN en.repartidor em " +
            "GROUP BY em.id, em.nombre " +
            "ORDER BY totalEntregas DESC")
    List<Object[]> obtenerDesempeñoEmpleados();
}
