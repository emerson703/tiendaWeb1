package com.backus.proyecto.services;

import com.backus.proyecto.entity.Calificacion;
import com.backus.proyecto.repository.CalificacionRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import com.backus.proyecto.util.interfaces.CrudServiceLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService implements CrudService<Calificacion> {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Override
    public List<Calificacion> listar() {
        return calificacionRepository.findAll();
    }

    @Override
    public Optional<Calificacion> buscarPorId(Integer id) {
        return calificacionRepository.findById(id);  // Aquí cambiamos Optional.empty() por findById.
    }

    @Override
    public Calificacion guardar(Calificacion entity) {
        return calificacionRepository.save(entity);
    }

    @Override
    public boolean existePorId(Integer id) {
        return calificacionRepository.existsById(id);  // Deberías verificar si existe por el ID.
    }

    @Override
    public void eliminarPorId(Integer id) {
        calificacionRepository.deleteById(id);
    }
}
