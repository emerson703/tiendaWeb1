package com.backus.proyecto.services;

import com.backus.proyecto.entity.Producto;
import com.backus.proyecto.util.interfaces.CrudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service

public class ProductoService implements CrudService<Producto> {
    @Autowired
    private com.backus.proyecto.repository.ProductoRepository ProductoRepository;
    @Override
    public List<Producto> listar() {
        return  ProductoRepository.findAll();
    }

    @Override
    public Optional<Producto> buscarPorId(Integer id) {
        Optional<Producto> ProductoOptional = ProductoRepository.findById(id);
        if (ProductoOptional.isPresent()) {
            return Optional.of(ProductoOptional.get());
        } else {
            return Optional.empty(); // Devolver un Optional vacío en caso de que no se encuentre la categoría
        }
    }

    @Override
    public Producto guardar(Producto entity) {
        return  ProductoRepository.save(entity);
    }


    @Override
    public boolean existePorId(Integer id) {
        return ProductoRepository.findById(id).isPresent();

    }

    @Override
    public void eliminarPorId(Integer id) {
        ProductoRepository.deleteById(id);
    }

    public List<Producto> productosActivos() {
     return   ProductoRepository.findAll().stream()
                .filter(producto -> producto.isVisible()) // Asumiendo que visible es un booleano
                .collect(Collectors.toList());
    }
}
