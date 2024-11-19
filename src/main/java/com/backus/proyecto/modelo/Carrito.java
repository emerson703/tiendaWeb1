package com.backus.proyecto.modelo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Carrito {
    private List<ProductoRequest> productos;

    public Carrito() {
        this.productos = new ArrayList<>();
    }

    public List<ProductoRequest> getProductos() {
        return productos;
    }

    public void agregarProducto(ProductoRequest producto) {
        this.productos.add(producto);
    }

    public int getCantidadTotalProductos() {
        int cantidadTotal = 0;
        for (ProductoRequest producto : productos) {
            cantidadTotal += producto.getCantidad();
        }
        return cantidadTotal;
    }

    public Map<Integer, Integer> getCantidadPorProducto() {
        Map<Integer, Integer> cantidadPorProducto = new HashMap<>();
        for (ProductoRequest producto : productos) {
            cantidadPorProducto.put(producto.getIdProducto(), producto.getCantidad());
        }
        return cantidadPorProducto;
    }

    public double getSumatoriaTotal() {
        double sumatoriaTotal = 0.0;
        for (ProductoRequest producto : productos) {
            sumatoriaTotal += producto.getPrecioTotal();
        }
        return sumatoriaTotal;
    }

    public int getCantidadTiposProductos() {
        return productos.size(); // Retorna la cantidad de tipos diferentes de productos (cantidad de elementos en la lista)
    }
}
