package com.backus.proyecto.services;

import com.backus.proyecto.entity.Carrito;
import com.backus.proyecto.entity.DetalleCarrito;
import com.backus.proyecto.entity.Producto;

import com.backus.proyecto.modelo.ProductoRequest;
import com.backus.proyecto.repository.CarritoRepository;
import com.backus.proyecto.repository.DetalleCarritoRepository;
import com.backus.proyecto.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarritoService {
    @Autowired
    private CarritoRepository carritoRepository;

    @Autowired
    private DetalleCarritoRepository detalleCarritoRepository; // Asume que tienes un repositorio de detalles

    @Autowired
    private ProductoRepository productoRepository;


}
