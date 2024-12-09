package com.backus.proyecto.services;

import com.backus.proyecto.entity.Marca;
import com.backus.proyecto.entity.transporte;
import com.backus.proyecto.repository.MarcaRepository;
import com.backus.proyecto.repository.TransporteRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class TransporteService implements CrudService <transporte> {

    @Autowired
    private TransporteRepository transporteRepository;
    @Override
    public List<transporte> listar() {
        return  transporteRepository.findAll();
    }

    @Override
    public Optional<transporte> buscarPorId(Integer id) {
        Optional<transporte> TransOptional = transporteRepository.findById(id);
        if (TransOptional.isPresent()) {
            return Optional.of(TransOptional.get());
        } else {
            return Optional.empty(); // Devolver un Optional vacío en caso de que no se encuentre la categoría
        }
    }

    @Override
    public transporte guardar(transporte entity) {
        return  transporteRepository.save(entity);
    }


    @Override
    public boolean existePorId(Integer id) {
        return transporteRepository.findById(id).isPresent();

    }

    @Override
    public void eliminarPorId(Integer id) {
        transporteRepository.deleteById(id);
    }
}
