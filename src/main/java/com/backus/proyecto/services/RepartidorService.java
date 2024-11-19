package com.backus.proyecto.services;

import com.backus.proyecto.entity.Repartidor;
import com.backus.proyecto.repository.RepartidorRepository;
import com.backus.proyecto.util.interfaces.CrudServiceLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
@Service
public class RepartidorService implements CrudServiceLong <Repartidor> {

    @Autowired
    private RepartidorRepository repartidorRepository;

    @Override
    public List<Repartidor> listar() {
        return repartidorRepository.findAll();
    }

    @Override
    public Optional<Repartidor> buscarPorId(Long id) {
        Optional <Repartidor> repartidorOptional = repartidorRepository.findById(id);
        return repartidorOptional.isPresent() ? Optional.of(repartidorOptional.get()) : Optional.empty();
    }

    @Override
    public Repartidor guardar(Repartidor entity) {
        return repartidorRepository.save(entity);
    }

    @Override
    public boolean existePorId(Long id) {
        return repartidorRepository.findById(id).isPresent();
    }

    @Override
    public void eliminarPorId(Long id) {
        repartidorRepository.deleteById(id);
    }
}
