package com.backus.proyecto.services;

import com.backus.proyecto.entity.Categoria;
import com.backus.proyecto.repository.CategoriaRepository;
import com.backus.proyecto.util.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class CategoriaService implements CrudService<Categoria> {
    @Autowired
    private CategoriaRepository categoriaRepository;
    @Override
    public List<Categoria> listar() {
        return  categoriaRepository.findAll();
    }

    @Override
    public Optional<Categoria> buscarPorId(Integer id) {
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(id);
        if (categoriaOptional.isPresent()) {
            return Optional.of(categoriaOptional.get());
        } else {
            return Optional.empty(); // Devolver un Optional vacío en caso de que no se encuentre la categoría
        }
    }

    @Override
    public Categoria guardar(Categoria entity) {
        return  categoriaRepository.save(entity);
    }


    @Override
    public boolean existePorId(Integer id) {
        return categoriaRepository.findById(id).isPresent();

    }

    @Override
    public void eliminarPorId(Integer id) {
        categoriaRepository.deleteById(id);
    }


}
