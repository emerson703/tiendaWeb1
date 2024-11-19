package com.backus.proyecto.services;

import com.backus.proyecto.entity.Detalle;
import com.backus.proyecto.repository.DetalleRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DetalleService implements CrudService<Detalle> {
    @Autowired
    private DetalleRepository detalleRepository;
    @Override
    public List<Detalle> listar() {
        return  detalleRepository.findAll();
    }

    @Override
    public Optional<Detalle> buscarPorId(Integer id) {
        return Optional.empty();
    }

    public List<Detalle> buscarPorPedido(Integer id) {
        return detalleRepository.findByPedido_IdPedido(Long.valueOf(id));
    }


    @Override
    public Detalle guardar(Detalle entity) {
        return  detalleRepository.save(entity);
    }

    @Override
    public boolean existePorId(Integer id) {
        return false;
    }


    @Override
    public void eliminarPorId(Integer id) {

    }


}
