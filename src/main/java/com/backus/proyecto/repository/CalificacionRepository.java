package com.backus.proyecto.repository;

import com.backus.proyecto.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {

    @Query(value = "SELECT c.calificacion, " +
            "COUNT(*) AS cantidad_calificaciones, " +
            "GROUP_CONCAT(c.comentario SEPARATOR '; ') AS comentarios_agrupados " +
            "FROM calificacion c " +
            "GROUP BY c.calificacion " +
            "ORDER BY c.calificacion DESC",
            nativeQuery = true)
    List<Object[]> obtenerReporteComentariosPorCalificacion();
}
