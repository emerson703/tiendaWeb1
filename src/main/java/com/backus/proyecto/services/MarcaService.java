package com.backus.proyecto.services;

import com.backus.proyecto.entity.Marca;
import com.backus.proyecto.repository.MarcaRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class MarcaService implements CrudService<Marca> {
    @Autowired
    private MarcaRepository MarcaRepository;
    @Override
    public List<Marca> listar() {
        return  MarcaRepository.findAll();
    }

    @Override
    public Optional<Marca> buscarPorId(Integer id) {
        Optional<Marca> MarcaOptional = MarcaRepository.findById(id);
        if (MarcaOptional.isPresent()) {
            return Optional.of(MarcaOptional.get());
        } else {
            return Optional.empty(); // Devolver un Optional vacío en caso de que no se encuentre la categoría
        }
    }

    @Override
    public Marca guardar(Marca entity) {
        return  MarcaRepository.save(entity);
    }


    @Override
    public boolean existePorId(Integer id) {
        return MarcaRepository.findById(id).isPresent();

    }

    @Override
    public void eliminarPorId(Integer id) {
        MarcaRepository.deleteById(id);
    }


}
