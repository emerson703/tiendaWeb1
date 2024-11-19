package com.backus.proyecto.services;
import com.backus.proyecto.entity.Calificacion;
import com.backus.proyecto.repository.CalificacionRepository;
import com.backus.proyecto.util.interfaces.CrudServiceLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService implements CrudServiceLong<Calificacion> {
    @Autowired
    private CalificacionRepository calificacionRepository;

    @Override
    public List<Calificacion> listar() {
        return calificacionRepository.findAll();
    }

    @Override
    public Optional<Calificacion> buscarPorId(Long id) {
        return Optional.empty();
    }

    @Override
    public Calificacion guardar(Calificacion entity) {
        return calificacionRepository.save(entity);
    }

    @Override
    public boolean existePorId(Long id) {
        return false;
    }

    @Override
    public void eliminarPorId(Long id) {    calificacionRepository.deleteById(id);
    }
}
