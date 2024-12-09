package com.backus.proyecto.services;

import com.backus.proyecto.entity.Asignacion;
import com.backus.proyecto.entity.Repartidor;
import com.backus.proyecto.entity.transporte;
import com.backus.proyecto.repository.AsignacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AsignacionService {
    @Autowired
    private AsignacionRepository asignacionRepository;

    public Asignacion asignarTransporteARepartidor(transporte transporte, Repartidor repartidor) {
        Asignacion asignacion = new Asignacion();
        asignacion.setTransporte(transporte);
        asignacion.setRepartidor(repartidor);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        return asignacionRepository.save(asignacion);
    }

    public List<Asignacion> listarAsignaciones() {
        return asignacionRepository.findAll();
    }
    public Asignacion obtenerAsignacionPorId(Integer id) {
        return asignacionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Asignación no encontrada con ID: " + id));
    }
    public void eliminarAsignacion(Integer id) {
        asignacionRepository.deleteById(id);
    }
    public Asignacion actualizarAsignacion(Integer id, transporte transporte1, Repartidor repartidor) {
        Asignacion asignacion = obtenerAsignacionPorId(id);
        asignacion.setTransporte(transporte1);
        asignacion.setRepartidor(repartidor);
        asignacion.setFechaAsignacion(LocalDateTime.now());
        return asignacionRepository.save(asignacion);
    }
}
